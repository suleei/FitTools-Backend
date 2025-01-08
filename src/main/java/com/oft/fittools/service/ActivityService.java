package com.oft.fittools.service;

import com.oft.fittools.dto.activity.ActivityCommentDTO;
import com.oft.fittools.dto.activity.GetActivitiesReqDTO;
import com.oft.fittools.dto.activity.SendCommentReqDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface ActivityService {
    Object getActivities(GetActivitiesReqDTO getActivitiesReqDTO);

    void thumbsUp(@NotNull(message = "点赞活动ID不能为空") Integer activityId);

    ActivityCommentDTO sendComment(SendCommentReqDTO sendCommentReqDTO);

    List<ActivityCommentDTO> getComments(@NotNull Integer activityId);

    void deleteComment(@NotNull Integer commentId);
}
