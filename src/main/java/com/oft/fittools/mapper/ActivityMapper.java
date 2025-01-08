package com.oft.fittools.mapper;

import com.oft.fittools.dto.activity.GetActivityRespDTO;
import com.oft.fittools.po.Activity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface ActivityMapper {
    void insert(Activity activity);

    List<GetActivityRespDTO> getActivities(Integer userId, Date startTime, Integer id);

    Integer getActivityUserIdByActivityId(Integer activityId);

}
