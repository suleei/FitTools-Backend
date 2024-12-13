package com.oft.fittools.dto;

import lombok.Data;

@Data
public class BasicUserInfoDTO {
    String username;
    String password;
    String eMail;
    String phoneNumber;
    String identifyingCode;
    String jwt;
}
