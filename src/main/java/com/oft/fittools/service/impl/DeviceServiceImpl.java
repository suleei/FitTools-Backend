package com.oft.fittools.service.impl;

import com.oft.fittools.dto.device.AddDeviceReqDTO;
import com.oft.fittools.dto.device.DeviceDTO;
import com.oft.fittools.dto.device.GetDevicesRespDTO;
import com.oft.fittools.mapper.DeviceMapper;
import com.oft.fittools.mapper.UserMapper;
import com.oft.fittools.po.Device;
import com.oft.fittools.po.User;
import com.oft.fittools.service.DeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {
    private final UserMapper userMapper;
    private final DeviceMapper deviceMapper;

    public DeviceServiceImpl(UserMapper userMapper, DeviceMapper deviceMapper) {
        this.userMapper = userMapper;
        this.deviceMapper = deviceMapper;
    }

    @Override
    public void insert(AddDeviceReqDTO addDeviceReqDTO) {
        User user = userMapper.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Device device = new Device();
        device.setUser_id(user.getId());
        BeanUtils.copyProperties(addDeviceReqDTO, device);
        deviceMapper.insert(device);
    }

    @Override
    public GetDevicesRespDTO getDevices() {
        User user=userMapper.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Device> devices = deviceMapper.getDeviceByUserId(user.getId());
        GetDevicesRespDTO getDevicesRespDTO = new GetDevicesRespDTO();
        List<DeviceDTO> list = new ArrayList<>();
        for(Device device:devices){
            DeviceDTO getDeviceRespDTO = new DeviceDTO();
            BeanUtils.copyProperties(device,getDeviceRespDTO);
            list.add(getDeviceRespDTO);
        }
        getDevicesRespDTO.setDevices(list);
        getDevicesRespDTO.setDefaultDevice(user.getDevice_id());
        return getDevicesRespDTO;
    }

    @Override
    public void deleteDevice(int id) {
        User user=userMapper.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        deviceMapper.delete(id, user.getId());
    }

    @Override
    public void setDefaultDevice(int deviceId) {
        User user=userMapper.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(deviceMapper.getDeviceByIdAndUserId(deviceId,user.getId())==null) throw new RuntimeException("您不拥有该设备");
        if(user.getDevice_id()!=null&&user.getDevice_id()==deviceId) userMapper.setDevice(null, user.getId());
        else userMapper.setDevice(deviceId, user.getId());
    }
}
