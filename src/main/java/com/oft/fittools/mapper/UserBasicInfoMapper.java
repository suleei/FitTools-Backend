package com.oft.fittools.mapper;

import com.oft.fittools.po.UserBasicInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserBasicInfoMapper {
    UserBasicInfo getUserBasicInfoByUsername(String username);
}
