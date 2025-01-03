package com.oft.fittools.mapper;

import com.oft.fittools.po.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface MessageMapper {

    public void insert(ChatMessage chatMessage);

    public List<ChatMessage> getHistoryMessages(String source, String target);

    public List<ChatMessage> getHistoryMessagesBefore(String source, String target, Date before);
}
