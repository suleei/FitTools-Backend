package com.oft.fittools.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.system.UserInfo;
import com.oft.fittools.dto.user.EmailModificationReqDTO;
import com.oft.fittools.dto.user.EmailSendingReqDTO;
import com.oft.fittools.dto.user.NewEmailSendingReqDTO;
import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/avatar")
    public ResponseResult avatarUpload(@RequestParam MultipartFile file) {
        userService.uploadAvatar(file);
        return ResponseResult.success();
    }

    @GetMapping
    public ResponseResult getUserInfo() {
        return ResponseResult.success(userService.getUserInfo());
    }

    @PutMapping("/username")
    public ResponseResult updateUsername(@RequestParam @NotBlank @Size(max = 20) String username) {
        return ResponseResult.success(userService.updateUsername(username));
    }

    @GetMapping("/email/captcha")
    public ResponseResult sendEmailCaptcha(@Validated EmailSendingReqDTO emailSendingReqDTO){
        userService.sendEmailCaptcha(emailSendingReqDTO);
        return ResponseResult.success();
    }

    @GetMapping("/email/verify")
    public ResponseResult verifyEmail(@RequestParam @Size(min=6, max=6) String captcha){
        return ResponseResult.success(userService.verify(captcha));
    }

    @GetMapping("/email/captcha/new")
    public ResponseResult sendEmailCaptcha(@Validated NewEmailSendingReqDTO newEmailSendingReqDTO){
        userService.sendNewEmailCaptcha(newEmailSendingReqDTO);
        return ResponseResult.success();
    }

    @PostMapping("/email")
    public ResponseResult modifyEmail(@RequestBody @Validated EmailModificationReqDTO emailModificationReqDTO){
        userService.modifyEmail(emailModificationReqDTO);
        return ResponseResult.success();
    }
}
