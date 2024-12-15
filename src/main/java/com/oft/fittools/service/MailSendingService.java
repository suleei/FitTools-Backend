package com.oft.fittools.service;

public interface MailSendingService {

    public void sendCaptcha(String destination, String operation);

    public boolean verifyCaptcha(String email, String captcha);
}
