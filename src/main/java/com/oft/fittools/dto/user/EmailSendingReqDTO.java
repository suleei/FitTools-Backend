package com.oft.fittools.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailSendingReqDTO {
    @NotBlank(message="必须指定验证码哈希")
    private String captchaHash;
    @NotBlank(message="验证码不能为空")
    private String captchaCode;
}
