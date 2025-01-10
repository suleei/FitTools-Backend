package com.oft.fittools.controller.restful;

import cn.hutool.poi.excel.cell.CellSetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oft.fittools.dto.activity.GetActivitiesReqDTO;
import com.oft.fittools.dto.activity.SendCommentReqDTO;
import com.oft.fittools.global.ResponseResult;
import com.oft.fittools.global.UserContextHolder;
import com.oft.fittools.mapper.ActivityMapper;
import com.oft.fittools.service.ActivityService;
import com.oft.fittools.service.CommentNotifyService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping
    public ResponseResult getActivities(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime, Integer id, Boolean commented) {
        if(commented) {
            if(startTime != null) return ResponseResult.success(new ArrayList<GetActivitiesReqDTO>());
            return ResponseResult.success(activityService.getCommentedActivities());
        }
        GetActivitiesReqDTO getActivitiesReqDTO = new GetActivitiesReqDTO(startTime, id);
        return ResponseResult.success(activityService.getActivities(getActivitiesReqDTO));
    }

    @GetMapping("/commented/cardinality")
    public ResponseResult getCommentedCardinality() {
        return ResponseResult.success(activityService.getCommentedCardinality());
    }

    @DeleteMapping("/commented")
    public ResponseResult confirmCommentedActivity(@NotNull(message = "待确认的活动ID不能为空") Integer activityId) {
        activityService.confirmCommentedActivity(activityId);
        return ResponseResult.success();
    }

    @DeleteMapping
    public ResponseResult deleteActivity(@NotNull(message = "要删除的活动ID不能为空") Integer id) {
        activityService.deleteActivity(id);
        return ResponseResult.success();
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
