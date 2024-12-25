package com.oft.fittools.controller.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
@ServerEndpoint("/ws/log_confirm_notify/{call_sign}")
public class LogConfirmNotifyingSocketServer {
    private static int onlineCount = 0;
    private Session session;
    private String call_sign;
    private static ConcurrentHashMap<String, LogConfirmNotifyingSocketServer> webSocketMap = new ConcurrentHashMap<>();

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
            addOnlineCount();
        }
    }

    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(call_sign)) {
            webSocketMap.remove(call_sign);
            subOnlineCount();
        }
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static void sendInfo(String message, String call_sign) throws IOException {
        if(StringUtils.isNotBlank(call_sign)&&webSocketMap.containsKey(call_sign)) {
            webSocketMap.get(call_sign).sendMessage(message);
        }
    }

    public static synchronized void addOnlineCount(){
        LogConfirmNotifyingSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount(){
        LogConfirmNotifyingSocketServer.onlineCount--;
    }

    public static synchronized int getOnlineCount(){
        return LogConfirmNotifyingSocketServer.onlineCount;
    }
}
