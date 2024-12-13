package com.oft.fittools.controller;

import com.oft.fittools.dto.req.EmailSendingReqDTO;
import com.oft.fittools.dto.req.UserRegistrationReqDTO;
import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    @GetMapping("/captcha")
    public ResponseResult captcha() {
        return ResponseResult.success(loginService.getCaptcha());
    }

    @PostMapping("/email")
    public ResponseResult emailSending(@RequestBody @Validated EmailSendingReqDTO emailSendingReqDTO) {
        loginService.eMailSending(emailSendingReqDTO);
        return ResponseResult.success();
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody @Validated UserRegistrationReqDTO userRegistrationReqDTO){
        loginService.register(userRegistrationReqDTO);
        return ResponseResult.success();
    }
}
