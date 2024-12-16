package com.oft.fittools.po;

import lombok.Data;

@Data
public class User {
    private int id;
    private String username;
    private String phone;
    private String email;
    private String nickname;
    private String password;
    private String avatar;
}
