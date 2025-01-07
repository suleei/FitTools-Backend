package com.oft.fittools.service;

import jakarta.validation.constraints.NotNull;

public interface ActivityService {
    Object getActivities();

    void thumbsUp(@NotNull(message = "点赞活动ID不能为空") Integer activityId);
}
