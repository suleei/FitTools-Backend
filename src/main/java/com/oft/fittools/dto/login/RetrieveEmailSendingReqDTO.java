package com.oft.fittools.dto.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RetrieveEmailSendingReqDTO {
    @NotBlank(message = "用户名不能为空")
    @Size(max = 20, message="用户名最大长度不能超过20")
    private String username;
    @NotBlank(message="邮箱不能为空")
    @Size(max=30,message = "邮箱最大长度为30位")
    private String email;
    @NotBlank(message="必须指定验证码哈希")
    private String captchaHash;
    @NotBlank(message="验证码不能为空")
    private String captchaCode;
}
