package com.oft.fittools.mapper;

import com.oft.fittools.po.CommunicationLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunicationLogMapper {
    public void insert(CommunicationLog communicationLog);

    public List<CommunicationLog> selectCommunicationLogByUserIdAndOffetLimit(Integer userId, int offset, int limit);

    public void delete(Integer logId, Integer userId);

    public CommunicationLog getLogDetail(Integer logId, Integer userId);
}
