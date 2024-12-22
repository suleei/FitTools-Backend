package com.oft.fittools.service;

import com.oft.fittools.dto.device.AddDeviceReqDTO;
import com.oft.fittools.dto.device.DeviceDTO;
import com.oft.fittools.dto.device.GetDevicesRespDTO;

import java.util.List;

public interface DeviceService {
    public void insert(AddDeviceReqDTO addDeviceReqDTO);

    public GetDevicesRespDTO getDevices();

    void deleteDevice(int id);

    void setDefaultDevice(int deviceId);
}
