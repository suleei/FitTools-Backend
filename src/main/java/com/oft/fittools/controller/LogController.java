package com.oft.fittools.controller;

import com.oft.fittools.dto.log.CommunicationLogDTO;
import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.CommunicationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogController {
    private final CommunicationLogService communicationLogService;
    @PostMapping
    public ResponseResult addLog(@RequestBody @Validated CommunicationLogDTO communicationLogDTO) {
        communicationLogService.insert(communicationLogDTO);
        return ResponseResult.success();
    }

    @GetMapping
    public ResponseResult getLogByPageNum(Integer pageNum) {
        return ResponseResult.success(communicationLogService.selectPage(pageNum));
    }
}
