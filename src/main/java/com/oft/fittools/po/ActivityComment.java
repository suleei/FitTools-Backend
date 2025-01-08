package com.oft.fittools.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ActivityComment {
    Integer id;
    Integer activityId;
    Integer ActivityUserId;
    Integer CommenterUserId;
    String commenterCallSign;
    String comment;
    Date time;
    String replyCallSign;
}
