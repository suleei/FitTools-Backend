package com.oft.fittools.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailModificationReqDTO {
    String jwt;
    @Size(max = 30)
    @Email
    String email;
    @Size(min = 6, max = 6)
    String captcha;
}
