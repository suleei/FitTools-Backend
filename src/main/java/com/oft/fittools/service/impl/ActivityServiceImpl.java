package com.oft.fittools.service.impl;

import com.oft.fittools.dto.activity.ActivityCommentDTO;
import com.oft.fittools.dto.activity.GetActivitiesReqDTO;
import com.oft.fittools.dto.activity.GetActivityRespDTO;
import com.oft.fittools.dto.activity.SendCommentReqDTO;
import com.oft.fittools.global.UserContextHolder;
import com.oft.fittools.mapper.ActivityCommentMapper;
import com.oft.fittools.mapper.ActivityMapper;
import com.oft.fittools.mapper.ActivityThumbsUpMapper;
import com.oft.fittools.mapper.UserMapper;
import com.oft.fittools.po.ActivityComment;
import com.oft.fittools.po.User;
import com.oft.fittools.service.ActivityService;
import com.oft.fittools.service.CommentNotifyService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityMapper activityMapper;
    private final ActivityThumbsUpMapper activityThumbsUpMapper;
    private final ActivityCommentMapper activityCommentMapper;
    private final UserMapper userMapper;
    private final ExecutorService executor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
    private final CommentNotifyService commentNotifyService;;
    @Value("${minio.endpoint}")
    private String endpoint;
    @Override
    public List<GetActivityRespDTO> getActivities(GetActivitiesReqDTO getActivitiesReqDTO) {
        List<GetActivityRespDTO> activities = activityMapper.getActivities(UserContextHolder.getUser().getId(), getActivitiesReqDTO.getStartTime(), getActivitiesReqDTO.getId());
        return activityPostProcess(activities);
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

    @Override
    public ActivityCommentDTO sendComment(SendCommentReqDTO sendCommentReqDTO) {
        if(StringUtils.isBlank(UserContextHolder.getUser().getCall_sign())) throw new RuntimeException("用户呼号不能为空");
        ActivityComment comment = new ActivityComment();
        BeanUtils.copyProperties(sendCommentReqDTO, comment);
        Integer activityUserId = activityMapper.getActivityUserIdByActivityId(comment.getActivityId());
        comment.setActivityUserId(activityUserId);
        if(comment.getActivityUserId()==null) throw new RuntimeException("活动不存在");
        comment.setCommenterUserId(UserContextHolder.getUser().getId());
        comment.setCommenterCallSign(UserContextHolder.getUser().getCall_sign());
        activityCommentMapper.addComment(comment);
        ActivityCommentDTO activityCommentDTO = new ActivityCommentDTO();
        BeanUtils.copyProperties(comment, activityCommentDTO);
        activityCommentDTO.setDeletable(true);
        String avatarURL = endpoint + "/avatar/";
        if(UserContextHolder.getUser().getAvatar()!=null)
            activityCommentDTO.setCommenterAvatar(avatarURL + UserContextHolder.getUser().getAvatar());
        else
            activityCommentDTO.setCommenterAvatar(avatarURL + "avatar.png");

        Integer userID = UserContextHolder.getUser().getId();
        executor.submit(()->{
            if(activityUserId != userID){
                User activityUser = userMapper.getUserById(activityUserId);
                commentNotifyService.notify(activityUser.getCall_sign(), sendCommentReqDTO.getActivityId());
            }
            if(StringUtils.isNotBlank(sendCommentReqDTO.getReplyCallSign())){
                commentNotifyService.notify(sendCommentReqDTO.getReplyCallSign(), sendCommentReqDTO.getActivityId());
            }
        });
        return activityCommentDTO;
    }

    @Override
    public List<ActivityCommentDTO> getComments(Integer activityId) {
        String avatarURL = endpoint + "/avatar/";
        List<ActivityCommentDTO> comments = activityCommentMapper.getComments(activityId, UserContextHolder.getUser().getId());
        for(ActivityCommentDTO comment : comments) {
            if(comment.getCommenterAvatar()!=null)
                comment.setCommenterAvatar(avatarURL + comment.getCommenterAvatar());
            else
                comment.setCommenterAvatar(avatarURL + "avatar.png");
        }
        return comments;
    }

    @Override
    public void deleteComment(Integer commentId) {
        if(activityCommentMapper.deleteComment(UserContextHolder.getUser().getId(),commentId)==0) throw new RuntimeException("评论不存在或没有权限删除该条评论");
    }

    @Override
    public void deleteActivity(Integer id) {
        Integer res=activityMapper.delete(id, UserContextHolder.getUser().getId());
        if(res==0) throw new RuntimeException("指定活动不存在或用户没有权限");
    }

    @Override
    public List<GetActivityRespDTO> getCommentedActivities() {
        Set<Integer> ids = commentNotifyService.getCommentedActivityIds(UserContextHolder.getUser().getCall_sign());
        if(ids.isEmpty()) return new ArrayList<>();
        List<GetActivityRespDTO> activities = activityMapper.getCommentedActivities(UserContextHolder.getUser().getId(), ids);
        return activityPostProcess(activities);
    }

    @Override
    public Long getCommentedCardinality() {
        return commentNotifyService.getNotify(UserContextHolder.getUser().getCall_sign());
    }

    @Override
    public void confirmCommentedActivity(Integer activityId) {
        commentNotifyService.confirm(activityId);
    }

    private List<GetActivityRespDTO> activityPostProcess(List<GetActivityRespDTO> activities) {
        String avatarURL = endpoint + "/avatar/";
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
}
