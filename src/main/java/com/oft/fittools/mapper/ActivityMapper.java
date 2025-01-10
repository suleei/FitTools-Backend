package com.oft.fittools.mapper;

import com.oft.fittools.dto.activity.GetActivityRespDTO;
import com.oft.fittools.po.Activity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Mapper
public interface ActivityMapper {
    void insert(Activity activity);

    Integer delete(Integer id, Integer userId);

    List<GetActivityRespDTO> getActivities(Integer userId, Date startTime, Integer id);

    List<GetActivityRespDTO> getCommentedActivities(Integer userId, Set<Integer> ids);

    Integer getActivityUserIdByActivityId(Integer activityId);

}
