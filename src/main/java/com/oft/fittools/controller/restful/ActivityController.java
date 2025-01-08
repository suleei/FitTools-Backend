package com.oft.fittools.controller.restful;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oft.fittools.dto.activity.GetActivitiesReqDTO;
import com.oft.fittools.dto.activity.SendCommentReqDTO;
import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.service.ActivityService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping
    public ResponseResult getActivities(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime, Integer id) {
        GetActivitiesReqDTO getActivitiesReqDTO = new GetActivitiesReqDTO(startTime, id);
        return ResponseResult.success(activityService.getActivities(getActivitiesReqDTO));
    }

    @PutMapping("/thumbs")
    public ResponseResult thumbsUp(@NotNull(message = "点赞活动ID不能为空") Integer activityId){
        activityService.thumbsUp(activityId);
        return ResponseResult.success();
    }

    @PostMapping("/comment")
    public ResponseResult sendComment(@RequestBody @Validated SendCommentReqDTO sendCommentReqDTO){
        return ResponseResult.success(activityService.sendComment(sendCommentReqDTO));
    }

    @GetMapping("/comment")
    public ResponseResult getComments(@NotNull Integer activityId){
        return ResponseResult.success(activityService.getComments(activityId));
    }

    @DeleteMapping("/comment")
    public ResponseResult deleteComment(@NotNull Integer commentId){
        activityService.deleteComment(commentId);
        return ResponseResult.success();
    }
}
