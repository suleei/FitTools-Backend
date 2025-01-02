package com.oft.fittools.dto.chat;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessageDTO {
    Date time;
    String source;
    String target;
    String message;
}
