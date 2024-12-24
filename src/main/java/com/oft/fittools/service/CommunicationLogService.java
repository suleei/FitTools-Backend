package com.oft.fittools.service;

import com.oft.fittools.dto.log.CommunicationLogDTO;
import com.oft.fittools.dto.log.CommunicationLogPageDTO;

import java.util.List;

public interface CommunicationLogService {
    void insert(CommunicationLogDTO communicationLog);

    List<CommunicationLogPageDTO> selectPage(Integer page);

    void deleteLog(Integer logId);

    CommunicationLogDTO getLogDetail(Integer logId);

    List<CommunicationLogPageDTO> selectGuestPage(Integer pageNum);

    void acceptLog(Integer logId);

    void rejectLog(Integer logId);

    CommunicationLogDTO getGuestLogDetail(Integer logId);
}
