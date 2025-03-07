package com.oft.fittools.global;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oft.fittools.dto.chat.ChatMessageDTO;
import com.oft.fittools.dto.chat.MessageNotifyDTO;
import lombok.Data;

@Data
public class SocketInfo {
    enum InfoType {
        NEW_CONFIRM_MESSAGE_NUM,
        CHAT_MESSAGE,
        TARGET_ONLINE_STATUS,
        MESSAGE_NOTIFY,
        COMMENT_NOTIFY
    }

    InfoType type;
    String formattedMessage;

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String newConfirmMessageNum(Long num) throws JsonProcessingException {
        SocketInfo socketInfo = new SocketInfo();
        socketInfo.type = InfoType.NEW_CONFIRM_MESSAGE_NUM;
        socketInfo.formattedMessage = num.toString();
        return objectMapper.writeValueAsString(socketInfo);
    }

    public static String newChatMessage(ChatMessageDTO chatMessageDTO) throws JsonProcessingException{
        SocketInfo socketInfo = new SocketInfo();
        socketInfo.type = InfoType.CHAT_MESSAGE;
        socketInfo.formattedMessage = JSON.toJSONString(chatMessageDTO);
        return JSON.toJSONString(socketInfo);
    }

    public static String newTargetOnlineStatus(Boolean status) throws JsonProcessingException{
        SocketInfo socketInfo = new SocketInfo();
        socketInfo.type = InfoType.TARGET_ONLINE_STATUS;
        socketInfo.formattedMessage =   objectMapper.writeValueAsString(status);
        return objectMapper.writeValueAsString(socketInfo);
    }

    public static String newMessageNotify(MessageNotifyDTO messageNotifyDTO) throws JsonProcessingException{
        SocketInfo socketInfo = new SocketInfo();
        socketInfo.type = InfoType.MESSAGE_NOTIFY;
        socketInfo.formattedMessage =   JSON.toJSONString(messageNotifyDTO);
        return objectMapper.writeValueAsString(socketInfo);
    }

    public static String newCommentNotify(Long cardinality) throws JsonProcessingException{
        SocketInfo socketInfo = new SocketInfo();
        socketInfo.type = InfoType.COMMENT_NOTIFY;
        socketInfo.formattedMessage =   JSON.toJSONString(cardinality);
        return objectMapper.writeValueAsString(socketInfo);
    }
}
