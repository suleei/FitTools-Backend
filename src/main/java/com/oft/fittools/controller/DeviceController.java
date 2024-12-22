package com.oft.fittools.controller;

import com.oft.fittools.dto.device.AddDeviceReqDTO;
import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;
    @PostMapping
    public ResponseResult addDevice(@RequestBody @Validated AddDeviceReqDTO addDeviceReqDTO) {
        deviceService.insert(addDeviceReqDTO);
        return ResponseResult.success();
    }

    @GetMapping
    public ResponseResult getDevices() {
        return ResponseResult.success(deviceService.getDevices());
    }

    @DeleteMapping
    public ResponseResult deleteDevice(@RequestParam int deviceId) {
        deviceService.deleteDevice(deviceId);
        return ResponseResult.success();
    }

    @PostMapping("/default")
    public ResponseResult setDefaultDevice(@RequestParam int deviceId) {
        deviceService.setDefaultDevice(deviceId);
        return ResponseResult.success();
    }
}
