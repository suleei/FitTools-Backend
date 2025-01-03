package com.oft.fittools.mq;

import com.oft.fittools.dto.chat.ChatMessageDTO;
import com.oft.fittools.mapper.MessageMapper;
import com.oft.fittools.po.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RocketMQMessageListener(consumerGroup = "CHAT_MESSAGE_PROCESSING_CONSUMER", topic = "CHAT_MESSAGE_PROCESSING")
@RequiredArgsConstructor
public class ChatMessageConsumer implements RocketMQListener<ChatMessageDTO> {
    private final MessageMapper messageMapper;
    private final RocketMQTemplate rocketMQTemplate;

    @Override
    @Transactional
    public void onMessage(ChatMessageDTO chatMessageDTO) {
        ChatMessage chatMessage = new ChatMessage();
        BeanUtils.copyProperties(chatMessageDTO, chatMessage);
        messageMapper.insert(chatMessage);
        Message<ChatMessageDTO> message = MessageBuilder.withPayload(chatMessageDTO).build();
        Long targetTime = System.currentTimeMillis() + 2*60*1000;
        rocketMQTemplate.syncSendDeliverTimeMills("CHAT_MESSAGE_DELAY_DELETING", message, targetTime);
    }
}
