package com.oft.fittools.mapper;

import com.oft.fittools.po.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper {

    public void insert(ChatMessage chatMessage);
}
