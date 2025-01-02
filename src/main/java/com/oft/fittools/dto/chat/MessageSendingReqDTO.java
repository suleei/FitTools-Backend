package com.oft.fittools.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class MessageSendingReqDTO {

    @NotNull(message = "消息时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date time;
    @NotBlank(message = "目标用户不能为空")
    @Size(min=1, max=10,message = "目标用户字符长度为1到10")
    String target;
    @NotBlank(message = "消息不能为空")
    @Size(max = 100,message = "消息长度最大为100字符")
    String message;
}
