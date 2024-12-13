package com.oft.fittools.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailSendingReqDTO {
    @NotBlank(message="邮箱不能为空")
    @Size(max=30,message = "邮箱最大长度为30位")
    private String email;
    @NotBlank(message="必须指定验证码哈希")
    private String captchaHash;
    @NotBlank(message="验证码不能为空")
    private String captchaCode;
}
