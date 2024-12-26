package com.oft.fittools.controller.restful;

import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.NearByHamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ham")
@RequiredArgsConstructor
public class NearByHam {
    private final NearByHamService nearByHamService;

    @GetMapping("/status")
    public ResponseResult getActiveStatus(){
        return ResponseResult.success(nearByHamService.getActiveStatus());
    }
}
