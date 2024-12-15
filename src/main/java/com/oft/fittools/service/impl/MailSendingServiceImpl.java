package com.oft.fittools.service.impl;

import com.oft.fittools.service.MailSendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MailSendingServiceImpl implements MailSendingService {

    private final JavaMailSender javaMailSender;
    private final StringRedisTemplate stringRedisTemplate;
    private final String emailKeyPrefix = "email:";
    @Value("${spring.mail.username}")
    private String eMailSender;
    private final ExecutorService executorService = new ThreadPoolExecutor(5,5,2, TimeUnit.MINUTES,new LinkedBlockingQueue<>(), new ThreadPoolExecutor.DiscardPolicy());


    @Override
    public void sendCaptcha(String destination, String operation) {
        executorService.submit(() -> {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(eMailSender);
            simpleMailMessage.setTo(destination);
            simpleMailMessage.setSubject("[Open Fit Tools] 邮箱验证码");
            String identifyingCode=String.valueOf(new Random().nextInt(900000)+100000);
            simpleMailMessage.setText("您正在进行"+operation+"，验证码为"+identifyingCode+ "，有效时间：5分钟。");
            javaMailSender.send(simpleMailMessage);
            stringRedisTemplate.opsForValue().set(emailKeyPrefix+ destination,identifyingCode,5, TimeUnit.MINUTES);
        });
    }

    @Override
    public boolean verifyCaptcha(String email, String captcha) {
        return captcha.equals(stringRedisTemplate.opsForValue().get(emailKeyPrefix+email));
    }


}
