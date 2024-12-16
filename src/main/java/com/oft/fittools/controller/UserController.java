package com.oft.fittools.controller;

import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.UserService;
import lombok.RequiredArgsConstructor;
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

}
