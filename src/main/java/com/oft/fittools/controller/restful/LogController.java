package com.oft.fittools.controller.restful;

import com.oft.fittools.dto.log.CommunicationLogDTO;
import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.CommunicationLogService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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

    @DeleteMapping
    public ResponseResult deleteLog(Integer logId){
        communicationLogService.deleteLog(logId);
        return ResponseResult.success();
    }

    @GetMapping("/detail")
    public ResponseResult getLogDetail(Integer logId){
        return ResponseResult.success(communicationLogService.getLogDetail(logId));
    }

    @GetMapping("/guest")
    public ResponseResult getGuestLogByPageNum(Integer pageNum) {
        return ResponseResult.success(communicationLogService.selectGuestPage(pageNum));
    }

    @GetMapping("/guest/detail")
    public ResponseResult getGuestLogDetail(Integer logId){
        return ResponseResult.success(communicationLogService.getGuestLogDetail(logId));
    }

    @PutMapping("/accept")
    public ResponseResult acceptLog(@NotNull Integer logId){
        communicationLogService.acceptLog(logId);
        return ResponseResult.success();
    }

    @PutMapping("/reject")
    public ResponseResult rejectLog(@NotNull Integer logId){
        communicationLogService.rejectLog(logId);
        return ResponseResult.success();
    }
}
