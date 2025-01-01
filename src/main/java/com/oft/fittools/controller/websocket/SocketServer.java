package com.oft.fittools.controller.websocket;

import cn.hutool.json.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.oft.fittools.mapper.UserMapper;
import com.oft.fittools.po.User;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

import java.io.IOException;
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
        System.out.println("onOpen: " + call_sign);
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
            webSocketMap.remove(call_sign);
            /*subOnlineCount();*/
        }
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
                System.out.println(signatureKey);
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
            }
        }
    }

    public static void sendInfo(String message, String call_sign) throws IOException {
        if(StringUtils.isNotBlank(call_sign)&&webSocketMap.containsKey(call_sign)) {
            webSocketMap.get(call_sign).sendMessage(message);
        }
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
