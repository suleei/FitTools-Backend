package com.oft.fittools.dto.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class SendCommentReqDTO {
    @NotNull(message = "活动ID不能为空")
    Integer activityId;
    @NotNull(message = "评论时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    Date time;
    @NotBlank(message = "评论不能为空")
    @Size(min = 1,max = 100, message = "评论限制为1-100字符")
    String comment;

    @Size(min = 1, max = 10, message = "回复对象的呼号长度为1-10字符")
    String replyCallSign;
}
