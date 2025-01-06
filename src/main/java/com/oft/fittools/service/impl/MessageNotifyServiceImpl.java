package com.oft.fittools.service.impl;

import com.oft.fittools.controller.websocket.SocketServer;
import com.oft.fittools.dto.chat.MessageNotifyDTO;
import com.oft.fittools.global.SocketInfo;
import com.oft.fittools.global.UserContextHolder;
import com.oft.fittools.service.MessageNotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MessageNotifyServiceImpl implements MessageNotifyService {

    private final StringRedisTemplate stringRedisTemplate;
    private final String prefix = "message_notify:";

    @Override
    public void notify(String user_call_sign, String guest_call_sign) {
        stringRedisTemplate.opsForSet().add(prefix + user_call_sign,guest_call_sign);
        Long cardinality = stringRedisTemplate.opsForSet().size(prefix + user_call_sign);
        try {
            SocketServer.sendInfo(SocketInfo.newMessageNotify(new MessageNotifyDTO(guest_call_sign, cardinality)), user_call_sign);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void confirm(String targetCallSign) {
        stringRedisTemplate.opsForSet().remove(prefix + UserContextHolder.getUser().getCall_sign(),targetCallSign);
        Long cardinality = stringRedisTemplate.opsForSet().size(prefix + UserContextHolder.getUser().getCall_sign());
        try {
            SocketServer.sendInfo(SocketInfo.newMessageNotify(new MessageNotifyDTO("", cardinality)), UserContextHolder.getUser().getCall_sign());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Set<String> getGuestSet() {
        return stringRedisTemplate.opsForSet().members(prefix + UserContextHolder.getUser().getCall_sign());
    }

    @Override
    public Long getNotify() {
        return stringRedisTemplate.opsForSet().size(prefix + UserContextHolder.getUser().getCall_sign());
    }
}
