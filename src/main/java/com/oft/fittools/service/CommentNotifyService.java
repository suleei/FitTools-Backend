package com.oft.fittools.service;

import java.util.List;
import java.util.Set;

public interface CommentNotifyService {

    void notify(String targetCallSign, Integer activityId);

    Long getNotify(String targetCallSign);

    void confirm(Integer activityId);

    Set<Integer> getCommentedActivityIds(String targetCallSign);
}
