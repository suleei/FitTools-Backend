package com.oft.fittools.controller.websocket;

import cn.hutool.json.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.oft.fittools.global.SocketInfo;
import com.oft.fittools.mapper.UserMapper;
import com.oft.fittools.po.User;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
@ServerEndpoint("/ws/{call_sign}")
public class SocketServer {

    private static String signatureKey;
    private static UserMapper userMapper;
    /*private static int onlineCount = 0;*/
    private Session session;
    private String call_sign;
    private Boolean authenticated = false;
    private String active_target;
    private Set<String> communicators = new HashSet<>();
    private static ConcurrentHashMap<String, SocketServer> webSocketMap = new ConcurrentHashMap<>();

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        SocketServer.userMapper = userMapper;
    }

    @Value("${jwt.signatureKey}")
    public void setSignatureKey(String signatureKey) {
        SocketServer.signatureKey = signatureKey;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("call_sign") String call_sign) {
        this.session = session;
        this.call_sign = call_sign;
        if(webSocketMap.containsKey(call_sign)) {
            webSocketMap.remove(call_sign);
            webSocketMap.put(call_sign, this);
        }else{
            webSocketMap.put(call_sign, this);
            /*addOnlineCount();*/
        }
    }

    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(call_sign)) {
            String target = webSocketMap.get(call_sign).getActive_target();
            if(target!=null && webSocketMap.containsKey(target)) {
                webSocketMap.get(target).getCommunicators().remove(call_sign);
            }
            for(String target_call_sign:webSocketMap.get(call_sign).getCommunicators()) {
                try {
                    sendInfo(SocketInfo.newTargetOnlineStatus(false), target_call_sign);
                }catch (Exception e){
                    System.out.println(e);
                }
            }
            webSocketMap.remove(call_sign);
            /*subOnlineCount();*/
        }
    }

    public Set<String> getCommunicators() {
        return communicators;
    }

    @OnError
    public void onError(Session session, Throwable error) {
        webSocketMap.remove(call_sign);
    }

    public void sendMessage(String message) throws IOException {
        if(!this.authenticated) {
            return;
        }
        this.session.getBasicRemote().sendText(message);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        if(StringUtils.isNotBlank(message)) {
            JSONObject jsonObject = new JSONObject(message);
            if(!this.authenticated) {
                String jwt = jsonObject.getStr("jwt");
                JWTVerifier jwtVerifier= JWT.require(Algorithm.HMAC256(signatureKey)).build();
                String username=null;
                try {
                    DecodedJWT decodedJWT =  jwtVerifier.verify(jwt);
                    username=decodedJWT.getClaim("username").asString();
                }catch (Exception e){
                    sendMessage(e.getMessage());
                    return;
                }
                User user = userMapper.getUserByUsername(username);
                if(user==null){
                    sendMessage("用户未找到");
                    return;
                }
                if(!user.getCall_sign().equals(this.call_sign)){
                    sendMessage("用户呼号不匹配");
                    return;
                }
                this.authenticated = true;
            }else{
                if(!this.authenticated){
                    sendMessage("未授权");
                    return;
                }
                if(StringUtils.isNotBlank(jsonObject.getStr("active_target"))) {
                    String pre_target = this.active_target;
                    if(pre_target!=null && webSocketMap.containsKey(pre_target)) {
                        webSocketMap.get(pre_target).getCommunicators().remove(this.call_sign);
                    }
                    if(jsonObject.getStr("active_target").equals("CLOSE_CHAT")) {
                        this.active_target = null;
                        return;
                    }
                    this.active_target = jsonObject.getStr("active_target");
                    if(webSocketMap.containsKey(this.active_target)) {
                        webSocketMap.get(this.active_target).getCommunicators().add(this.call_sign);
                        sendMessage(SocketInfo.newTargetOnlineStatus(true));
                    }else{
                        sendMessage(SocketInfo.newTargetOnlineStatus(false));
                    }
                }
            }
        }
    }

    public static void sendInfo(String message, String call_sign) throws IOException {
        if(StringUtils.isNotBlank(call_sign)&&webSocketMap.containsKey(call_sign)) {
            webSocketMap.get(call_sign).sendMessage(message);
        }
    }

    public static void sendMessage(String message, String call_sign, String dst_call_sign) throws IOException {
        if(StringUtils.isNotBlank(dst_call_sign)&&webSocketMap.containsKey(dst_call_sign)&&call_sign.equals(webSocketMap.get(dst_call_sign).getActive_target())) {
            webSocketMap.get(dst_call_sign).sendMessage(message);
        }
    }

    public String getActive_target() {
        return active_target;
    }

    /*public static synchronized void addOnlineCount(){
        SocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount(){
        SocketServer.onlineCount--;
    }

    public static synchronized int getOnlineCount(){
        return SocketServer.onlineCount;
    }*/
}
