package com.oft.fittools.service.impl;

import com.alibaba.fastjson.JSON;
import com.oft.fittools.controller.websocket.SocketServer;
import com.oft.fittools.dto.chat.*;
import com.oft.fittools.global.SocketInfo;
import com.oft.fittools.global.UserContextHolder;
import com.oft.fittools.mapper.MessageMapper;
import com.oft.fittools.po.ChatMessage;
import com.oft.fittools.po.User;
import com.oft.fittools.service.ChatService;
import com.oft.fittools.service.MessageNotifyService;
import com.oft.fittools.service.NearByHamService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final StringRedisTemplate stringRedisTemplate;
    private final RocketMQTemplate rocketMQTemplate;
    private final MessageMapper messageMapper;
    private final MessageNotifyService messageNotifyService;
    private static final String prefix = "chat:";

    @Override
    public void sendMessage(MessageSendingReqDTO messageSendingReqDTO) {
        if(StringUtils.isBlank(UserContextHolder.getUser().getCall_sign())) throw new RuntimeException("呼号未设置");
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        BeanUtils.copyProperties(messageSendingReqDTO, chatMessageDTO);
        chatMessageDTO.setSource(UserContextHolder.getUser().getCall_sign());
        try {
            if(!SocketServer.sendMessage(SocketInfo.newChatMessage(chatMessageDTO), chatMessageDTO.getSource(), chatMessageDTO.getTarget())){
                /*messageNotifyService.notify(chatMessageDTO.getTarget(),UserContextHolder.getUser().getCall_sign());*/
                chatMessageDTO.setNotify(true);
            }else{
                chatMessageDTO.setNotify(false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        String redisChatSetKey = chatMessageDTO.getSource() + ">" + chatMessageDTO.getTarget();
        stringRedisTemplate.opsForZSet().add(prefix + redisChatSetKey, JSON.toJSONString(chatMessageDTO), chatMessageDTO.getTime().getTime());

        Message<ChatMessageDTO> message = MessageBuilder.withPayload(chatMessageDTO).build();
        rocketMQTemplate.syncSend("CHAT_MESSAGE_PROCESSING", message);
    }

    @Override
    public List<GetMessageRespDTO> getCachedMessages(String targetCallSign) {
        User user = UserContextHolder.getUser();
        String call_sign = user.getCall_sign();
        if(StringUtils.isBlank(call_sign)) throw new RuntimeException("用户呼号为空");
        String sendKey = call_sign + ">" + targetCallSign;
        String receiveKey = targetCallSign + ">" + call_sign;
        Set<String> sendElements = stringRedisTemplate.opsForZSet().range(prefix + sendKey, 0, -1);
        List<GetMessageRespDTO> sendMessages = new ArrayList<>();
        for(String sendMessage : sendElements) {
            ChatMessageDTO message = JSON.parseObject(sendMessage, ChatMessageDTO.class);
            sendMessages.add(new GetMessageRespDTO(message.getTime(),message.getMessage(),true));
        }
        Set<String> receiveElements = stringRedisTemplate.opsForZSet().range(prefix + receiveKey, 0, -1);
        List<GetMessageRespDTO> receiveMessages = new ArrayList<>();
        for(String receiveMessage : receiveElements) {
            ChatMessageDTO message = JSON.parseObject(receiveMessage, ChatMessageDTO.class);
            receiveMessages.add(new GetMessageRespDTO(message.getTime(),message.getMessage(),false));
        }
        List<GetMessageRespDTO> messages = new ArrayList<>(sendMessages.size()+receiveMessages.size());
        int sIndex = 0, rIndex = 0;
        while(sIndex < sendMessages.size()&&rIndex < receiveMessages.size()) {
            while(sIndex < sendMessages.size()&&sendMessages.get(sIndex).getTime().before(receiveMessages.get(rIndex).getTime())) {
                messages.add(sendMessages.get(sIndex));
                sIndex++;
            }
            while(sIndex < sendMessages.size()&&rIndex < receiveMessages.size()&&receiveMessages.get(rIndex).getTime().before(sendMessages.get(sIndex).getTime())) {
                messages.add(receiveMessages.get(rIndex));
                rIndex++;
            }
        }
        while(sIndex < sendMessages.size()) {
            messages.add(sendMessages.get(sIndex));
            sIndex++;
        }
        while(rIndex < receiveMessages.size()) {
            messages.add(receiveMessages.get(rIndex));
            rIndex++;
        }
        return messages;
    }

    @Override
    public List<GetMessageRespDTO> getHistoryMessages(GetHistoryMessagesReqDTO getHistoryMessagesReqDTO) {
        User user = UserContextHolder.getUser();
        String source = user.getCall_sign();
        if(StringUtils.isBlank(source)) throw new RuntimeException("用户呼号不能为空");
        List<ChatMessage> messages;
        if(getHistoryMessagesReqDTO.getTimeBefore()==null){
            messages=messageMapper.getHistoryMessages(source, getHistoryMessagesReqDTO.getTargetCallSign());
        }else{
            messages=messageMapper.getHistoryMessagesBefore(source, getHistoryMessagesReqDTO.getTargetCallSign(), getHistoryMessagesReqDTO.getTimeBefore());
        }
        List<GetMessageRespDTO> res = new ArrayList<>();
        for(ChatMessage message : messages) {
            res.add(new GetMessageRespDTO(message.getTime(),message.getMessage(),message.getSource().equals(source)));
        }
        return res;
    }

    @Override
    public List<GetChatListRespDTO> getChatList() {
        String source = UserContextHolder.getUser().getCall_sign();
        if(StringUtils.isBlank(source)) throw new RuntimeException("用户呼号不能为空");
        List<GetChatListRespDTO> res = messageMapper.getChatList(source);
        Set<String> set = messageNotifyService.getGuestSet();
        for(GetChatListRespDTO respDTO : res) {
            if(set.contains(respDTO.getTargetCallSign())){
                respDTO.setHasNewMessage(true);
            }else respDTO.setHasNewMessage(false);
        }
        return res;
    }
}
