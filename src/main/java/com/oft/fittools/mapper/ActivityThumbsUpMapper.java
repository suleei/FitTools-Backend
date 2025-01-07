package com.oft.fittools.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActivityThumbsUpMapper {
    void addThumbsUp(Integer userId, Integer activityId);

    Integer getThumbsUp(Integer userId, Integer activityId);

    void deleteThumbsUp(Integer id);
}
