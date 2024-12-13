package com.oft.fittools.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ni")
public class Test {
    @GetMapping("/hao")
    public String get(){
        return "sssss";
    }
}
