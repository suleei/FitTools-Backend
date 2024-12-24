package com.oft.fittools.service.impl;

import com.oft.fittools.dto.log.CommunicationLogDTO;
import com.oft.fittools.dto.log.CommunicationLogPageDTO;
import com.oft.fittools.mapper.CommunicationLogMapper;
import com.oft.fittools.mapper.UserMapper;
import com.oft.fittools.po.CommunicationLog;
import com.oft.fittools.po.User;
import com.oft.fittools.service.CommunicationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunicationLogServiceImpl implements CommunicationLogService {
    private final UserMapper userMapper;
    private final CommunicationLogMapper communicationLogMapper;

    @Override
    public void insert(CommunicationLogDTO communicationLog) {
        communicationLog.setDuration(communicationLog.getEnd_time().getTime() - communicationLog.getStart_time().getTime());
        if(communicationLog.getDuration()<=0) throw new RuntimeException("结束时间必须大于开始时间");
        User user = userMapper.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        CommunicationLog log = new CommunicationLog();
        BeanUtils.copyProperties(communicationLog, log);
        log.setConfirm_status('N');
        log.setUser_id(user.getId());
        communicationLogMapper.insert(log);
    }

    @Override
    public List<CommunicationLogPageDTO> selectPage(Integer page) {
        if(page == null) page=1;
        User user = userMapper.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        int pageCount = 20;
        int offset = (page - 1) * pageCount;
        List<CommunicationLog> list = communicationLogMapper.selectCommunicationLogByUserIdAndOffetLimit(user.getId(),offset,pageCount);
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
        User user = userMapper.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        communicationLogMapper.delete(logId, user.getId());
    }

    @Override
    public CommunicationLogDTO getLogDetail(Integer logId) {
        User user = userMapper.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        CommunicationLog log = communicationLogMapper.getLogDetail(logId, user.getId());
        CommunicationLogDTO dto = new CommunicationLogDTO();
        BeanUtils.copyProperties(log,dto);
        return dto;
    }
}
