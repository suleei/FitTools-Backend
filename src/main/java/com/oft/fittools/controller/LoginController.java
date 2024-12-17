package com.oft.fittools.controller;

import com.oft.fittools.dto.login.*;
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
    @PostMapping("/register/email")
    public ResponseResult emailSending(@RequestBody @Validated RegistrationEmailSendingReqDTO registrationEmailSendingReqDTO) {
        loginService.eMailSending(registrationEmailSendingReqDTO);
        return ResponseResult.success();
    }

    @PostMapping("/retrieve/email")
    public ResponseResult retrieveEmailSending(@RequestBody @Validated RetrieveEmailSendingReqDTO retrieveEmailSendingReqDTO) {
        loginService.retrieveEmailSending(retrieveEmailSendingReqDTO);
        return ResponseResult.success();
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody @Validated UserRegistrationReqDTO userRegistrationReqDTO){
        loginService.register(userRegistrationReqDTO);
        return ResponseResult.success();
    }

    @PostMapping("/retrieve")
    public ResponseResult retrieve(@RequestBody @Validated UserRetrieveReqDTO userRetrieveReqDTO){
        loginService.retrieve(userRetrieveReqDTO);
        return ResponseResult.success();
    }

    @PostMapping
    public ResponseResult login(@RequestBody @Validated UserLoginReqDTO loginReqDTO){
        return ResponseResult.success(loginService.login(loginReqDTO));
    }
}
