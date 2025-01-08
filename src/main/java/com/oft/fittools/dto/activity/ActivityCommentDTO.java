package com.oft.fittools.dto.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ActivityCommentDTO {
    Integer id;
    String commenterCallSign;
    String commenterAvatar;
    String comment;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    Date time;
    Boolean deletable;
    String replyCallSign;
}
