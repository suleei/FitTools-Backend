package com.oft.fittools.controller.restful;

import com.oft.fittools.dto.chat.MessageSendingReqDTO;
import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.ChatService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @GetMapping("/message/cached")
    public ResponseResult getCachedMessages(@NotBlank @Size(min=1,max = 10) String target_call_sign) {
        return ResponseResult.success(chatService.getCachedMessages(target_call_sign));
    }

}
