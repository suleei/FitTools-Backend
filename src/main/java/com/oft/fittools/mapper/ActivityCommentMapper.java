package com.oft.fittools.mapper;

import com.oft.fittools.dto.activity.ActivityCommentDTO;
import com.oft.fittools.po.ActivityComment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActivityCommentMapper {
    void addComment(ActivityComment comment);

    List<ActivityCommentDTO> getComments(int activityId, int userId);

    Integer deleteComment(int userId,int commentId);
}
