package com.oft.fittools.controller;

import com.oft.fittools.dto.BasicUserInfoDTO;
import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult<BasicUserInfoDTO> get(@RequestParam String username,@RequestParam String password){
        return ResponseResult.success(userService.login(username,password));
    }
}
