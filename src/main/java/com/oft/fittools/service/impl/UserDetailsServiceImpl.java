package com.oft.fittools.service.impl;

import com.oft.fittools.mapper.UserBasicInfoMapper;
import com.oft.fittools.po.UserBasicInfo;
import com.oft.fittools.security.BasicUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    private final UserBasicInfoMapper userBasicInfoMapper;

    @Autowired
    public UserDetailsServiceImpl(UserBasicInfoMapper userBasicInfoMapper){
        this.userBasicInfoMapper = userBasicInfoMapper;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserBasicInfo userBasicInfo= userBasicInfoMapper.getUserBasicInfoByUsername(username);
        if(userBasicInfo == null) throw new RuntimeException("用户名或密码错误");
        return new BasicUserDetails(userBasicInfo);
    }

}
