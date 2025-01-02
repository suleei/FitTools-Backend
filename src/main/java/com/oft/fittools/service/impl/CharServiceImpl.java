package com.oft.fittools.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oft.fittools.controller.websocket.SocketServer;
import com.oft.fittools.dto.chat.MessageDTO;
import com.oft.fittools.dto.chat.MessageSendingReqDTO;
import com.oft.fittools.global.SocketInfo;
import com.oft.fittools.global.UserContextHolder;
import com.oft.fittools.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CharServiceImpl implements ChatService {
    private final StringRedisTemplate stringRedisTemplate;
    private static final String prefix = "chat:";

    @Override
    public void sendMessage(MessageSendingReqDTO messageSendingReqDTO) {
        if(StringUtils.isBlank(UserContextHolder.getUser().getCall_sign())) throw new RuntimeException("呼号未设置");
        MessageDTO messageDTO = new MessageDTO();
        BeanUtils.copyProperties(messageSendingReqDTO, messageDTO);
        messageDTO.setSource(UserContextHolder.getUser().getCall_sign());
        try {
            SocketServer.sendMessage(SocketInfo.newChatMessage(messageDTO),messageDTO.getSource(),messageDTO.getTarget());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        String redisChatSetKey = messageDTO.getSource() + ">" + messageDTO.getTarget();
        stringRedisTemplate.opsForZSet().add(prefix + redisChatSetKey, messageDTO.getMessage(), messageDTO.getTime().getTime());
    }
}
