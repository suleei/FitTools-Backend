package com.oft.fittools.mapper;

import com.oft.fittools.dto.login.UserRegistrationReqDTO;
import com.oft.fittools.po.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void userRegister(UserRegistrationReqDTO userRegistrationReqDTO);

    int userEmailMatch(String username, String email);

    void updatePassword(String username, String password);

    User getUserByUsername(String username);

    void updateAvatar(String username, String avatar);

    void updateUsername(String oldName, String newName);
}
