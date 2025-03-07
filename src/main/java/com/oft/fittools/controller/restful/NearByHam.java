package com.oft.fittools.controller.restful;

import com.oft.fittools.dto.ham.SetStatusActiveReqDTo;
import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.NearByHamService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ham")
@RequiredArgsConstructor
public class NearByHam {
    private final NearByHamService nearByHamService;

    @GetMapping("/status")
    public ResponseResult getActiveStatus(){
        return ResponseResult.success(nearByHamService.getActiveStatus());
    }

    @PostMapping("/status")
    public ResponseResult setStatusActive(@RequestBody SetStatusActiveReqDTo setStatusActiveReqDTo){
        nearByHamService.setStatusActive(setStatusActiveReqDTo);
        return ResponseResult.success();
    }

    @DeleteMapping("/status")
    public ResponseResult setStatusInactive(){
        nearByHamService.setStatusInactive();
        return ResponseResult.success();
    }

    @GetMapping
    public ResponseResult getNearByHam(@NotNull(message = "距离不能为空") @Min(value = 5, message = "最小为五公里")  @Max(value = 6000, message = "最大为六千公里") Integer distance){
        return ResponseResult.success(nearByHamService.getNearByHam(distance));
    }

    @GetMapping("/time")
    public ResponseResult getActiveTime(){
        return ResponseResult.success(nearByHamService.getActiveTime());
    }

    @PostMapping("/time")
    public ResponseResult getActiveTime(@RequestBody SetStatusActiveReqDTo setStatusActiveReqDTo){
        nearByHamService.updateActiveTime(setStatusActiveReqDTo);
        return ResponseResult.success();
    }
}
