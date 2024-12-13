package com.oft.fittools.dto.req;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegistrationReqDTO {

    @NotBlank(message="用户名不能为空")
    @Size(max=20,message = "用户名最大长度为20位")
    private String username;
    @NotBlank(message="密码不能为空")
    @Size(max=72,message = "密码最大长度为72位")
    private String password;
    @NotBlank(message="邮箱不能为空")
    @Size(max=30,message = "邮箱最大长度为30位")
    private String email;
    @NotBlank(message="邮箱验证码不能为空")
    @Size(min=6, max=6,message = "邮箱验证码长度为6位")
    private String verificationCode;

    private String nickname;
}
