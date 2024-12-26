package com.oft.fittools.service.impl;

import com.oft.fittools.controller.websocket.LogConfirmNotifyingSocketServer;
import com.oft.fittools.global.UserContextHolder;
import com.oft.fittools.mapper.UserMapper;
import com.oft.fittools.po.User;
import com.oft.fittools.service.LogConfirmNotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class LogConfirmNotifyServiceImpl implements LogConfirmNotifyService {

    private final StringRedisTemplate stringRedisTemplate;
    private final String prefix = "confirm_log_counter:";
    private final UserMapper userMapper;

    @Override
    public void notify(String call_sign) {
        Long logCount = stringRedisTemplate.opsForValue().increment(prefix+call_sign, 1);
        try {
            LogConfirmNotifyingSocketServer.sendInfo(String.valueOf(logCount), call_sign);
        } catch (IOException e) {
        }
    }

    @Override
    public String getConfirmLog() {
        User user = UserContextHolder.getUser();
        if(stringRedisTemplate.hasKey(prefix+user.getCall_sign())){
            return stringRedisTemplate.opsForValue().get(prefix+user.getCall_sign());
        }
        return "0";
    }

    @Override
    public void deleteConfirmLog() {
        User user = UserContextHolder.getUser();
        stringRedisTemplate.delete(prefix+user.getCall_sign());
    }
}
