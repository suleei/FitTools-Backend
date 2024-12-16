package com.oft.fittools.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GetUserInfoRespDTO {
    private String username;
    private String email;
    private String nickname;
    private String avatar;
}
