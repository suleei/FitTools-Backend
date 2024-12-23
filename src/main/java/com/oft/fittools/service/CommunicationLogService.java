package com.oft.fittools.service;

import com.oft.fittools.dto.log.CommunicationLogDTO;

import java.util.List;

public interface CommunicationLogService {
    public void insert(CommunicationLogDTO communicationLog);

    public List<CommunicationLogDTO> selectPage(Integer page);
}
