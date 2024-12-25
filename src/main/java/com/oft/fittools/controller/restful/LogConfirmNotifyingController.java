package com.oft.fittools.controller.restful;


import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.LogConfirmNotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/confirm")
@RequiredArgsConstructor
public class LogConfirmNotifyingController {
    private final LogConfirmNotifyService logConfirmNotifyService;

    @GetMapping
    public ResponseResult getConfirmLog(){
        return ResponseResult.success(logConfirmNotifyService.getConfirmLog());
    }

    @DeleteMapping
    public ResponseResult deleteConfirmLog(){
        logConfirmNotifyService.deleteConfirmLog();
        return ResponseResult.success();
    }
}
