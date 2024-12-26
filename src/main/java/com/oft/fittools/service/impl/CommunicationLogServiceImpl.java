package com.oft.fittools.service.impl;

import com.oft.fittools.dto.log.CommunicationLogDTO;
import com.oft.fittools.dto.log.CommunicationLogPageDTO;
import com.oft.fittools.global.UserContextHolder;
import com.oft.fittools.mapper.CommunicationLogMapper;
import com.oft.fittools.mapper.UserMapper;
import com.oft.fittools.po.CommunicationLog;
import com.oft.fittools.po.User;
import com.oft.fittools.service.CallSignBloomFilterService;
import com.oft.fittools.service.CommunicationLogService;
import com.oft.fittools.service.LogConfirmNotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class CommunicationLogServiceImpl implements CommunicationLogService {
    private final UserMapper userMapper;
    private final CommunicationLogMapper communicationLogMapper;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final LogConfirmNotifyService logConfirmNotifyService;
    private final CallSignBloomFilterService callSignBloomFilterService;
    private final RedisTemplate redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void insert(CommunicationLogDTO communicationLog) {
        communicationLog.setDuration(communicationLog.getEnd_time().getTime() - communicationLog.getStart_time().getTime());
        if(communicationLog.getDuration()<=0) throw new RuntimeException("结束时间必须大于开始时间");
        Date now = new Date();
        if(now.getTime()<communicationLog.getEnd_time().getTime()) throw new RuntimeException("结束时间不能是未来某一时刻");
        User user = UserContextHolder.getUser();
        CommunicationLog log = new CommunicationLog();
        BeanUtils.copyProperties(communicationLog, log);
        log.setConfirm_status('N');
        log.setUser_id(user.getId());
        communicationLogMapper.insert(log);
        executor.submit(() -> {
            boolean match = false;
            if(callSignBloomFilterService.contains(log.getTarget_call_sign())){
                Date nowTime = new Date();
                if(nowTime.getTime()-log.getStart_time().getTime() < 24*60*60*1000){
                    String script = "if redis.call('exists', KEYS[1]) == 1 then local res = redis.call('get', KEYS[1]); redis.call('del', KEYS[1]); return res; elseif redis.call('exists', KEYS[2]) == 1 then local res = redis.call('get', KEYS[2]); redis.call('del', KEYS[2]); return res; elseif redis.call('exists', KEYS[3]) == 1 then local res = redis.call('get', KEYS[3]); redis.call('del', KEYS[3]); return res; else redis.call('set', KEYS[4], ARGV[1], 'EX', 60*60*25) end";
                    DefaultRedisScript<String> redisScript = new DefaultRedisScript<>(script, String.class);
                    String serachPair = communicationLog.getTarget_call_sign() + ">" + communicationLog.getSource_call_sign();
                    String setPair = communicationLog.getSource_call_sign() + ">" + communicationLog.getTarget_call_sign();
                    Long logSeconds = log.getStart_time().getTime() / 60000;
                    List<String> keys = new ArrayList<>();
                    keys.add(serachPair+ ">" + (logSeconds - 1));
                    keys.add(serachPair+ ">" + logSeconds);
                    keys.add(serachPair+ ">" + (logSeconds + 1));
                    keys.add(setPair+ ">" + logSeconds);
                    String matchLogId = stringRedisTemplate.execute(redisScript, keys,String.valueOf(log.getId()));
                    if(matchLogId!=null) {
                        communicationLogMapper.setConfirmStatusWarn(Integer.valueOf(matchLogId), 'Y');
                        communicationLogMapper.setConfirmStatusWarn(log.getId(), 'Y');
                        match = true;
                    }
                }else{
                    List<CommunicationLog> matchLogs = communicationLogMapper.selectMatchLog(log.getSource_call_sign(),new Date(log.getStart_time().getTime() - 600000),new Date(log.getStart_time().getTime() + 600000),new Date(log.getEnd_time().getTime() - 600000),new Date(log.getEnd_time().getTime() + 600000));
                    if(matchLogs.size()>0) {
                        CommunicationLog firstLog = matchLogs.get(0);
                        communicationLogMapper.setConfirmStatusWarn(firstLog.getId(), 'Y');
                        communicationLogMapper.setConfirmStatusWarn(log.getId(), 'Y');
                        match = true;
                    }
                }
            }
            if(!match) logConfirmNotifyService.notify(log.getTarget_call_sign());
        });
    }

    @Override
    public List<CommunicationLogPageDTO> selectPage(Integer page) {
        if(page == null) page=1;
        User user = UserContextHolder.getUser();
        int pageCount = 20;
        int offset = (page - 1) * pageCount;
        List<CommunicationLog> list = communicationLogMapper.selectCommunicationLogByUserIdAndOffsetLimit(user.getId(),offset,pageCount);
        List<CommunicationLogPageDTO> dtoList = new ArrayList<>();
        for(CommunicationLog log : list){
            CommunicationLogPageDTO dto = new CommunicationLogPageDTO();
            BeanUtils.copyProperties(log,dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public void deleteLog(Integer logId) {
        User user = UserContextHolder.getUser();
        communicationLogMapper.delete(logId, user.getId());
    }

    @Override
    public CommunicationLogDTO getLogDetail(Integer logId) {
        User user = UserContextHolder.getUser();
        CommunicationLog log = communicationLogMapper.getLogDetail(logId, user.getId());
        CommunicationLogDTO dto = new CommunicationLogDTO();
        BeanUtils.copyProperties(log,dto);
        return dto;
    }

    @Override
    public List<CommunicationLogPageDTO> selectGuestPage(Integer pageNum) {
        if(pageNum == null) pageNum=1;
        User user = UserContextHolder.getUser();
        int pageCount = 20;
        int offset = (pageNum - 1) * pageCount;
        List<CommunicationLog> list = communicationLogMapper.selectGuestCommunicationLogByUserIdAndOffsetLimit(user.getCall_sign(),offset,pageCount);
        List<CommunicationLogPageDTO> dtoList = new ArrayList<>();
        for(CommunicationLog log : list){
            CommunicationLogPageDTO dto = new CommunicationLogPageDTO();
            dto.setId(log.getId());
            dto.setStart_time(log.getStart_time());
            dto.setTarget_call_sign(log.getSource_call_sign());
            dto.setTarget_address(log.getSource_district());
            dto.setDistance(log.getDistance());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public void acceptLog(Integer logId) {
        setConfirmStatus(logId, 'Y');
    }

    @Override
    public void rejectLog(Integer logId) {
        setConfirmStatus(logId, 'X');
    }

    @Override
    public CommunicationLogDTO getGuestLogDetail(Integer logId) {
        User user = UserContextHolder.getUser();
        CommunicationLog log = communicationLogMapper.getGuestLogDetail(logId, user.getCall_sign());
        CommunicationLogDTO dto = new CommunicationLogDTO();
        BeanUtils.copyProperties(log,dto);
        dto.setSource_address("");
        dto.setSource_lat(Math.floor(log.getSource_lat()));
        dto.setSource_lng(Math.floor(log.getSource_lng()));
        return dto;
    }


    public void setConfirmStatus(Integer logId, Character status) {
        User user = UserContextHolder.getUser();
        communicationLogMapper.setConfirmStatus(logId,user.getCall_sign(),status);
    }
}
