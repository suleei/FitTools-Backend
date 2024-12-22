package com.oft.fittools.mapper;

import com.oft.fittools.po.Device;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeviceMapper{
    public void insert(Device device);

    public List<Device> getDeviceByUserId(int userId);

    public Device getDeviceByIdAndUserId(int deviceId, int userId);

    public void delete(int deviceId, int userId);
}
