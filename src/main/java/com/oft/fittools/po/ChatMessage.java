package com.oft.fittools.po;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessage {
    Integer id;
    Date time;
    String source;
    String target;
    String message;
}
