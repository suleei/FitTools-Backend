package com.oft.fittools.mq;

import com.alibaba.fastjson.JSON;
import com.oft.fittools.dto.chat.ChatMessageDTO;
import com.oft.fittools.mapper.MessageMapper;
import com.oft.fittools.po.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(consumerGroup = "CHAT_MESSAGE_DELAY_DELETING_CONSUMER", topic = "CHAT_MESSAGE_DELAY_DELETING")
@RequiredArgsConstructor
public class ChatMessageDelayDeletingConsumer implements RocketMQListener<ChatMessageDTO> {
    private final MessageMapper messageMapper;
    private final RocketMQTemplate rocketMQTemplate;
    private static final String prefix = "chat:";
    private final StringRedisTemplate stringRedisTemplate;


    @Override
    public void onMessage(ChatMessageDTO chatMessageDTO) {
        String redisChatSetKey = chatMessageDTO.getSource() + ">" + chatMessageDTO.getTarget();
        stringRedisTemplate.opsForZSet().remove(prefix + redisChatSetKey, JSON.toJSONString(chatMessageDTO));
    }
}
