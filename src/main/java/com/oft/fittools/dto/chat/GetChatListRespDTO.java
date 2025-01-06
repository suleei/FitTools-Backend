package com.oft.fittools.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class GetChatListRespDTO {
    String targetCallSign;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    Date latestTime;
    String message;
    Boolean hasNewMessage;
}
