package com.oft.fittools.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oft.fittools.dto.chat.GetMessageRespDTO;
import com.oft.fittools.dto.chat.MessageSendingReqDTO;
import com.oft.fittools.po.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.IOException;
import java.util.List;


public interface ChatService {
    void sendMessage(MessageSendingReqDTO messageSendingReqDTO);

    List<GetMessageRespDTO> getCachedMessages(@NotBlank @Size(min=1,max = 10) String targetCallSign);
}
