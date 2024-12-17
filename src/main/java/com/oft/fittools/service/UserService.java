package com.oft.fittools.service;

import com.oft.fittools.dto.user.EmailModificationReqDTO;
import com.oft.fittools.dto.user.EmailSendingReqDTO;
import com.oft.fittools.dto.user.GetUserInfoRespDTO;
import com.oft.fittools.dto.user.NewEmailSendingReqDTO;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    void uploadAvatar(MultipartFile file);

    GetUserInfoRespDTO getUserInfo();

    String updateUsername(String username);

    void sendEmailCaptcha(EmailSendingReqDTO emailSendingReqDTO);

    String verify(String captcha);

    void modifyEmail(EmailModificationReqDTO emailModificationReqDTO);

    void sendNewEmailCaptcha(NewEmailSendingReqDTO newEmailSendingReqDTO);
}
