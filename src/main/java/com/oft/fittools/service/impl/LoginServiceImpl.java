package com.oft.fittools.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.Validator;
import com.oft.fittools.dto.resp.CaptchaRespDTO;
import com.oft.fittools.dto.req.EmailSendingReqDTO;
import com.oft.fittools.dto.req.UserRegistrationReqDTO;
import com.oft.fittools.mapper.UserMapper;
import com.oft.fittools.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final StringRedisTemplate stringRedisTemplate;
    private final String captchaKeyPrefix = "captcha:";
    private final String emailKeyPrefix = "email:";
    private final ExecutorService executorService = new ThreadPoolExecutor(5,5,2,TimeUnit.MINUTES,new LinkedBlockingQueue<>(), new ThreadPoolExecutor.DiscardPolicy());
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Value("${spring.mail.username}")
    private String eMailSender;

    @Override
    public CaptchaRespDTO getCaptcha() {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(120, 50);
        stringRedisTemplate.opsForValue().set(captchaKeyPrefix+lineCaptcha.hashCode(),lineCaptcha.getCode(),2, TimeUnit.MINUTES);
        return new CaptchaRespDTO(lineCaptcha.hashCode(), lineCaptcha.getImageBase64());
    }

    @Override
    public void eMailSending(EmailSendingReqDTO emailSendingReqDTO) {
        if(!Validator.isEmail(emailSendingReqDTO.getEmail())) throw new RuntimeException("邮箱格式非法");
        if(!emailSendingReqDTO.getCaptchaCode().equals(stringRedisTemplate.opsForValue().get(captchaKeyPrefix+emailSendingReqDTO.getCaptchaHash()))) throw new RuntimeException("输入验证码错误");
        stringRedisTemplate.delete(captchaKeyPrefix+emailSendingReqDTO.getCaptchaHash());
        executorService.submit(() -> {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(eMailSender);
            simpleMailMessage.setTo(emailSendingReqDTO.getEmail());
            simpleMailMessage.setSubject("[Open Fit Tools] 邮箱验证码");
            String identifyingCode=String.valueOf(new Random().nextInt(900000)+100000);
            simpleMailMessage.setText("您的验证码为："+identifyingCode+"，有效时间：5分钟。");
            javaMailSender.send(simpleMailMessage);
            stringRedisTemplate.opsForValue().set(emailKeyPrefix+emailSendingReqDTO.getEmail(),identifyingCode,5, TimeUnit.MINUTES);
        });
    }

    @Override
    public void register(UserRegistrationReqDTO userRegistrationReqDTO) {
        if(!userRegistrationReqDTO.getVerificationCode().equals(stringRedisTemplate.opsForValue().get(emailKeyPrefix+userRegistrationReqDTO.getEmail()))) throw new RuntimeException("邮箱验证码错误");
        userRegistrationReqDTO.setNickname(userRegistrationReqDTO.getUsername());
        userRegistrationReqDTO.setPassword(passwordEncoder.encode(userRegistrationReqDTO.getPassword()));
        userMapper.userRegister(userRegistrationReqDTO);
    }
}
