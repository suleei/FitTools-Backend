package com.oft.fittools.service;

import com.oft.fittools.dto.user.*;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    void uploadAvatar(MultipartFile file);

    GetUserInfoRespDTO getUserInfo();

    String updateUsername(String username);

    void sendEmailCaptcha(EmailSendingReqDTO emailSendingReqDTO);

    String verify(String captcha);

    void modifyEmail(EmailModificationReqDTO emailModificationReqDTO);

    void sendNewEmailCaptcha(NewEmailSendingReqDTO newEmailSendingReqDTO);

    void updateAddress(AddressDTO addressDTO);

    AddressDTO getAddress();
}
