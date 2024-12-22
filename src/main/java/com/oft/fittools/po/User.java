package com.oft.fittools.po;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String phone;
    private String email;
    private String nickname;
    private String password;
    private String avatar;
    private Integer location_id;
    private Integer device_id;
    private String call_sign;
}
