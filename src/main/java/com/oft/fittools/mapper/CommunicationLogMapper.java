package com.oft.fittools.mapper;

import com.oft.fittools.po.CommunicationLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface CommunicationLogMapper {
    public Integer insert(CommunicationLog communicationLog);

    public List<CommunicationLog> selectCommunicationLogByUserIdAndOffsetLimit(Integer userId, int offset, int limit);

    public void delete(Integer logId, Integer userId);

    public CommunicationLog getLogDetail(Integer logId, Integer userId);

    public CommunicationLog getGuestLogDetail(Integer logId, String targetCallSign);

    public List<CommunicationLog> selectGuestCommunicationLogByUserIdAndOffsetLimit(String targetCallSign, int offset, int limit);

    public void setConfirmStatus(Integer logId, String targetCallSign, Character status);

    public void setConfirmStatusWarn(Integer logId, Character status);

    public List<CommunicationLog> selectMatchLog(String targetCallSign, Date startBegin, Date startEnd, Date endBegin, Date endEnd);

}
