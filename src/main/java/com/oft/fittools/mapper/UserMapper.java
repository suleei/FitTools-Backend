package com.oft.fittools.mapper;

import com.oft.fittools.dto.login.UserRegistrationReqDTO;
import com.oft.fittools.po.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void userRegister(UserRegistrationReqDTO userRegistrationReqDTO);

    int userEmailMatch(String username, String email);

    void updatePassword(String username, String password);

    User getUserByUsername(String username);

    void updateAvatar(String username, String avatar);

    void updateUsername(String oldName, String newName);

    void updateEmail(String username, String email);

    void updateLocationId(String username, Integer locationId);

}
