package com.oft.fittools.service;

import com.oft.fittools.dto.resp.CaptchaRespDTO;
import com.oft.fittools.dto.req.EmailSendingReqDTO;
import com.oft.fittools.dto.req.UserRegistrationReqDTO;

public interface LoginService {

    public CaptchaRespDTO getCaptcha();

    public void eMailSending(EmailSendingReqDTO emailSendingReqDTO);

    public void register(UserRegistrationReqDTO userRegistrationReqDTO);
}
