package com.oft.fittools.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.oft.fittools.dto.BasicUserInfoDTO;
import com.oft.fittools.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UserServiceImpl implements UserService {
    private AuthenticationManager authenticationManager;
    @Value("${jwt.signatureKey}")
    private String signatureKey;
    @Autowired
    public UserServiceImpl(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }
    @Override
    public BasicUserInfoDTO login(String username,String password){
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        if(authentication==null) throw new RuntimeException("用户名或密码错误");
        Calendar expireTime = Calendar.getInstance();
        expireTime.add(Calendar.HOUR,24);
        String jwt = JWT.create().withClaim("username",username).withExpiresAt(expireTime.getTime()).sign(Algorithm.HMAC256(signatureKey));
        BasicUserInfoDTO basicUserInfoDTO = new BasicUserInfoDTO();
        basicUserInfoDTO.setUsername(username);
        basicUserInfoDTO.setJwt(jwt);
        return basicUserInfoDTO;
    }
}