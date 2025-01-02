package com.oft.fittools.service.impl;

import com.alibaba.fastjson.JSON;
import com.oft.fittools.controller.websocket.SocketServer;
import com.oft.fittools.dto.chat.ChatMessageDTO;
import com.oft.fittools.dto.chat.MessageSendingReqDTO;
import com.oft.fittools.global.SocketInfo;
import com.oft.fittools.global.UserContextHolder;
import com.oft.fittools.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final StringRedisTemplate stringRedisTemplate;
    private final RocketMQTemplate rocketMQTemplate;
    private static final String prefix = "chat:";

    @Override
    public void sendMessage(MessageSendingReqDTO messageSendingReqDTO) {
        if(StringUtils.isBlank(UserContextHolder.getUser().getCall_sign())) throw new RuntimeException("呼号未设置");
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        BeanUtils.copyProperties(messageSendingReqDTO, chatMessageDTO);
        chatMessageDTO.setSource(UserContextHolder.getUser().getCall_sign());
        try {
            SocketServer.sendMessage(SocketInfo.newChatMessage(chatMessageDTO), chatMessageDTO.getSource(), chatMessageDTO.getTarget());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        String redisChatSetKey = chatMessageDTO.getSource() + ">" + chatMessageDTO.getTarget();
        stringRedisTemplate.opsForZSet().add(prefix + redisChatSetKey, JSON.toJSONString(chatMessageDTO), chatMessageDTO.getTime().getTime());

        Message<ChatMessageDTO> message = MessageBuilder.withPayload(chatMessageDTO).build();
        rocketMQTemplate.syncSend("CHAT_MESSAGE_PROCESSING", message);
    }
}
