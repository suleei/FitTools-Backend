package com.oft.fittools.service;

import com.oft.fittools.dto.login.CaptchaRespDTO;

public interface CaptchaService {
    public CaptchaRespDTO getCaptcha();

    public void verifyCaptcha(String hash, String code);

}
