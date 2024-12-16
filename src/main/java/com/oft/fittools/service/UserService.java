package com.oft.fittools.service;

import com.oft.fittools.dto.user.GetUserInfoRespDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    void uploadAvatar(MultipartFile file);

    GetUserInfoRespDTO getUserInfo();

    String updateUsername(String username);
}
