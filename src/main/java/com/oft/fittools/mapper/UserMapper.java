package com.oft.fittools.mapper;

import com.oft.fittools.dto.req.UserRegistrationReqDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void userRegister(UserRegistrationReqDTO userRegistrationReqDTO);
}
