package com.oft.fittools.dto.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginReqDTO {
    @NotBlank(message="用户名不能为空")
    @Size(max=20,message = "用户名最大长度为20位")
    private String username;

    @NotBlank(message="密码不能为空")
    @Size(max=72,message = "密码最大长度为72位")
    private String password;
}
