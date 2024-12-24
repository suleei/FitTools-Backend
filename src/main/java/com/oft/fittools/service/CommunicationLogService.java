package com.oft.fittools.service;

import com.oft.fittools.dto.log.CommunicationLogDTO;
import com.oft.fittools.dto.log.CommunicationLogPageDTO;

import java.util.List;

public interface CommunicationLogService {
    public void insert(CommunicationLogDTO communicationLog);

    public List<CommunicationLogPageDTO> selectPage(Integer page);

    void deleteLog(Integer logId);

    Object getLogDetail(Integer logId);
}
