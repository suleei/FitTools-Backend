package com.oft.fittools.controller.restful;

import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/captcha")
@RequiredArgsConstructor
public class CaptchaController {
    private final CaptchaService captchaService;

    @GetMapping
    public ResponseResult captcha() {
        return ResponseResult.success(captchaService.getCaptcha());
    }
}
