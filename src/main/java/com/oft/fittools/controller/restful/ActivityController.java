package com.oft.fittools.controller.restful;

import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.ActivityService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping
    public ResponseResult getActivities(){
        return ResponseResult.success(activityService.getActivities());
    }

    @PutMapping("/thumbs")
    public ResponseResult thumbsUp(@NotNull(message = "点赞活动ID不能为空") Integer activityId){
        activityService.thumbsUp(activityId);
        return ResponseResult.success();
    }
}
