package com.oft.fittools.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oft.fittools.dto.chat.MessageSendingReqDTO;

import java.io.IOException;

public interface ChatService {
    void sendMessage(MessageSendingReqDTO messageSendingReqDTO);
}
