package com.oft.fittools.dto.login;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CaptchaRespDTO {

    private final int hashCode;

    private final String base64Img;
}
