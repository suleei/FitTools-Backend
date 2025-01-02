package com.oft.fittools.dto.chat;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ChatMessageDTO {
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    Date time;
    String source;
    String target;
    String message;
}
