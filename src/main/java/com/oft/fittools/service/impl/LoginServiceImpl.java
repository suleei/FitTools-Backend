package com.oft.fittools.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.Validator;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.oft.fittools.dto.login.*;
import com.oft.fittools.dto.login.CaptchaRespDTO;
import com.oft.fittools.mapper.UserMapper;
import com.oft.fittools.service.CaptchaService;
import com.oft.fittools.service.LoginService;
import com.oft.fittools.service.MailSendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final MailSendingService mailSendingService;
    @Value("${jwt.signatureKey}")
    private String signatureKey;
    private final AuthenticationManager authenticationManager;
    private final CaptchaService captchaService;


    @Override
    public void eMailSending(RegistrationEmailSendingReqDTO registrationEmailSendingReqDTO) {
        if(!Validator.isEmail(registrationEmailSendingReqDTO.getEmail())) throw new RuntimeException("邮箱格式非法");
        captchaService.verifyCaptcha(registrationEmailSendingReqDTO.getCaptchaHash(), registrationEmailSendingReqDTO.getCaptchaCode());
        mailSendingService.sendCaptcha(registrationEmailSendingReqDTO.getEmail(), "用户注册");
    }

    @Override
    public void retrieveEmailSending(RetrieveEmailSendingReqDTO retrieveEmailSendingReqDTO) {
        if(!Validator.isEmail(retrieveEmailSendingReqDTO.getEmail())) throw new RuntimeException("邮箱格式非法");
        captchaService.verifyCaptcha(retrieveEmailSendingReqDTO.getCaptchaHash(), retrieveEmailSendingReqDTO.getCaptchaCode());
        if(userMapper.userEmailMatch(retrieveEmailSendingReqDTO.getUsername(), retrieveEmailSendingReqDTO.getEmail())==0) throw new RuntimeException("用户名或邮箱错误");
        mailSendingService.sendCaptcha(retrieveEmailSendingReqDTO.getEmail(),"密码找回");
    }

    @Override
    public void register(UserRegistrationReqDTO userRegistrationReqDTO) {
        if(!Validator.isEmail(userRegistrationReqDTO.getEmail())) throw new RuntimeException("邮箱格式非法");
        if(!mailSendingService.verifyCaptcha(userRegistrationReqDTO.getEmail(), userRegistrationReqDTO.getVerificationCode())) throw new RuntimeException("邮箱验证码错误");
        userRegistrationReqDTO.setNickname(userRegistrationReqDTO.getUsername());
        userRegistrationReqDTO.setPassword(passwordEncoder.encode(userRegistrationReqDTO.getPassword()));
        userMapper.userRegister(userRegistrationReqDTO);
    }

    @Override
    public void retrieve(UserRetrieveReqDTO userRetrieveReqDTO) {
        if(!Validator.isEmail(userRetrieveReqDTO.getEmail())) throw new RuntimeException("邮箱格式非法");
        if(!mailSendingService.verifyCaptcha(userRetrieveReqDTO.getEmail(), userRetrieveReqDTO.getVerificationCode())) throw new RuntimeException("邮箱验证码错误");
        if(userMapper.userEmailMatch(userRetrieveReqDTO.getUsername(), userRetrieveReqDTO.getEmail())==0) throw new RuntimeException("用户名或邮箱错误");
        userMapper.updatePassword(userRetrieveReqDTO.getUsername(), passwordEncoder.encode(userRetrieveReqDTO.getPassword()));
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO loginReqDTO) {
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReqDTO.getUsername(), loginReqDTO.getPassword()));
        if(authentication==null) throw new RuntimeException("用户名或密码错误");
        Calendar expireTime = Calendar.getInstance();
        expireTime.add(Calendar.HOUR,24);
        String jwt = JWT.create().withClaim("username",loginReqDTO.getUsername()).withExpiresAt(expireTime.getTime()).sign(Algorithm.HMAC256(signatureKey));
        return new UserLoginRespDTO(loginReqDTO.getUsername(), jwt);
    }
}
