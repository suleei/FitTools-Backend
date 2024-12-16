package com.oft.fittools.service;

import com.oft.fittools.dto.login.*;
import com.oft.fittools.dto.login.CaptchaRespDTO;

public interface LoginService {

    public CaptchaRespDTO getCaptcha();

    public void eMailSending(RegistrationEmailSendingReqDTO registrationEmailSendingReqDTO);

    public void register(UserRegistrationReqDTO userRegistrationReqDTO);

    public void retrieveEmailSending(RetrieveEmailSendingReqDTO retrieveEmailSendingReqDTO);

    void retrieve(UserRetrieveReqDTO userRetrieveReqDTO);

    UserLoginRespDTO login(UserLoginReqDTO loginReqDTO);
}
