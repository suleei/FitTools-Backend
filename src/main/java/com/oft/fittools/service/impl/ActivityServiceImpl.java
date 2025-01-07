package com.oft.fittools.service.impl;

import com.oft.fittools.dto.activity.GetActivityRespDTO;
import com.oft.fittools.global.UserContextHolder;
import com.oft.fittools.mapper.ActivityMapper;
import com.oft.fittools.mapper.ActivityThumbsUpMapper;
import com.oft.fittools.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityMapper activityMapper;
    private final ActivityThumbsUpMapper activityThumbsUpMapper;
    @Value("${minio.endpoint}")
    private String endpoint;
    @Override
    public List<GetActivityRespDTO> getActivities() {
        String avatarURL = endpoint + "/avatar/";
        List<GetActivityRespDTO> activities = activityMapper.getActivities(UserContextHolder.getUser().getId());
        for(GetActivityRespDTO activity : activities) {
            activity.setDistance(Math.floor(activity.getDistance()));

            activity.setSourceLng(Double.valueOf(String.format("%.1f",activity.getSourceLng())));
            activity.setSourceLat(Double.valueOf(String.format("%.1f",activity.getSourceLat())));
            if(activity.getAvatar()!=null)
                activity.setAvatar(avatarURL + activity.getAvatar());
            else
                activity.setAvatar(avatarURL + "avatar.png");
        }
        return activities;
    }

    @Override
    public void thumbsUp(Integer activityId) {
        Integer thumbsUpId = activityThumbsUpMapper.getThumbsUp(UserContextHolder.getUser().getId(), activityId);
        if(thumbsUpId!=null) {
            activityThumbsUpMapper.deleteThumbsUp(thumbsUpId);
        }else{
            activityThumbsUpMapper.addThumbsUp(UserContextHolder.getUser().getId(), activityId);
        }
    }
}
