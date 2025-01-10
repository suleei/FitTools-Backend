package com.oft.fittools.service.impl;

import com.oft.fittools.controller.websocket.SocketServer;
import com.oft.fittools.global.SocketInfo;
import com.oft.fittools.global.UserContextHolder;
import com.oft.fittools.service.CommentNotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommentNotifyServiceImpl implements CommentNotifyService {
    private final String prefix = "comment_notify:";
    private final StringRedisTemplate stringRedisTemplate;
    @Override
    public void notify(String targetCallSign, Integer activityId) {
        stringRedisTemplate.opsForSet().add(prefix + targetCallSign, String.valueOf(activityId));
        Long cardinality = stringRedisTemplate.opsForSet().size(prefix + targetCallSign);
        try {
            SocketServer.sendInfo(SocketInfo.newCommentNotify(cardinality), targetCallSign);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Long getNotify(String targetCallSign) {
        return stringRedisTemplate.opsForSet().size(prefix + targetCallSign);
    }

    @Override
    public void confirm(Integer activityId){
        String targetCallSign = UserContextHolder.getUser().getCall_sign();
        stringRedisTemplate.opsForSet().remove(prefix + targetCallSign, String.valueOf(activityId));
        Long cardinality = stringRedisTemplate.opsForSet().size(prefix + targetCallSign);
        try {
            SocketServer.sendInfo(SocketInfo.newCommentNotify(cardinality), targetCallSign);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Set<Integer> getCommentedActivityIds(String targetCallSign) {
        Set<String> ids = stringRedisTemplate.opsForSet().members(prefix + targetCallSign);
        Set<Integer> activityIds = new HashSet<>();
        for (String id : ids) {
            activityIds.add(Integer.parseInt(id));
        }
        return activityIds;
    }
}
