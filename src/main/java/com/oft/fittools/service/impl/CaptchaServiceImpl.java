package com.oft.fittools.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.oft.fittools.dto.login.CaptchaRespDTO;
import com.oft.fittools.service.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {
    private final StringRedisTemplate stringRedisTemplate;
    private final String captchaKeyPrefix = "captcha:";

    @Override
    public CaptchaRespDTO getCaptcha() {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(120, 50);
        stringRedisTemplate.opsForValue().set(captchaKeyPrefix+lineCaptcha.hashCode(),lineCaptcha.getCode(),2, TimeUnit.MINUTES);
        return new CaptchaRespDTO(lineCaptcha.hashCode(), lineCaptcha.getImageBase64());
    }

    @Override
    public void verifyCaptcha(String hash, String code) {
        if(!code.equals(stringRedisTemplate.opsForValue().get(captchaKeyPrefix+ hash))) throw new RuntimeException("输入验证码错误");
        stringRedisTemplate.delete(captchaKeyPrefix+ hash);
    }


}
