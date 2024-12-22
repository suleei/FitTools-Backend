package com.oft.fittools.dto.device;

import lombok.Data;

import java.util.List;

@Data
public class GetDevicesRespDTO {
    List<DeviceDTO> devices;
    Integer defaultDevice;
}
