package com.oft.fittools.dto.resp;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CaptchaRespDTO {

    private final int hashCode;

    private final String base64Img;
}
