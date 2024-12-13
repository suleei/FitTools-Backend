package com.oft.fittools.service;

import com.oft.fittools.dto.BasicUserInfoDTO;

public interface UserService {
    public BasicUserInfoDTO login(String username,String password);
}
