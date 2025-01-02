package com.oft.fittools.controller.restful;

import com.oft.fittools.dto.chat.MessageSendingReqDTO;
import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/message")
    public ResponseResult sendMessage(@RequestBody MessageSendingReqDTO messageSendingReqDTO) {
        chatService.sendMessage(messageSendingReqDTO);
        return ResponseResult.success();
    }
}
