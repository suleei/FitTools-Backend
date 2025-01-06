package com.oft.fittools.controller.restful;

import com.alibaba.fastjson.annotation.JSONField;
import com.oft.fittools.dto.chat.GetHistoryMessagesReqDTO;
import com.oft.fittools.dto.chat.MessageSendingReqDTO;
import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.ChatService;
import com.oft.fittools.service.MessageNotifyService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final MessageNotifyService messageNotifyService;

    @PostMapping("/message")
    public ResponseResult sendMessage(@RequestBody MessageSendingReqDTO messageSendingReqDTO) {
        chatService.sendMessage(messageSendingReqDTO);
        return ResponseResult.success();
    }

    @GetMapping("/message/cached")
    public ResponseResult getCachedMessages(@NotBlank @Size(min=1,max = 10) String target_call_sign) {
        return ResponseResult.success(chatService.getCachedMessages(target_call_sign));
    }

    @GetMapping("/message/history")
    public ResponseResult getHistoryMessages(@Validated GetHistoryMessagesReqDTO getHistoryMessagesReqDTO) {
        return ResponseResult.success(chatService.getHistoryMessages(getHistoryMessagesReqDTO));
    }

    @GetMapping("/list")
    public ResponseResult getChatList(){
        return ResponseResult.success(chatService.getChatList());
    }

    @PutMapping("/notify")
    public ResponseResult notifyConfirm(@NotBlank(message = "目标呼号不能为空") @Size(min = 1,max = 10,message = "目标呼号为1到10字符") String targetCallSign){
        messageNotifyService.confirm(targetCallSign);
        return ResponseResult.success();
    }

    @GetMapping("/notify")
    public ResponseResult getNotify(){
        return ResponseResult.success(messageNotifyService.getNotify());
    }
}
