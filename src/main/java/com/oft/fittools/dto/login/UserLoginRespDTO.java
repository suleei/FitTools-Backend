package com.oft.fittools.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
public class UserLoginRespDTO {
    private String username;

    private String jwt;
}
