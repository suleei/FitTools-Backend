package com.oft.fittools.security;

import com.oft.fittools.mapper.UserMapper;
import com.oft.fittools.po.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userMapper.getUserByUsername(username);
        if(user == null) throw new RuntimeException("用户名或密码错误");
        return new BasicUserDetails(user);
    }

}
