package com.oft.fittools.global;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class SocketInfo {
    enum InfoType {
        NEW_CONFIRM_MESSAGE_NUM
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
}
