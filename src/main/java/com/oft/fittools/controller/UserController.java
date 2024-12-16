package com.oft.fittools.controller;

import cn.hutool.system.UserInfo;
import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @PutMapping("/username")
    public ResponseResult updateUsername(@RequestParam @NotBlank @Size(max = 20) String username) {
        return ResponseResult.success(userService.updateUsername(username));
    }

}
