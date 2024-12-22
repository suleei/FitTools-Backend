package com.oft.fittools.dto.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddDeviceReqDTO {
    @NotBlank(message="设备名不能为空")
    @Size(max=50,message="设备名长度不能超过50个字符")
    private String name;
    @NotNull
    private double power;
    private String antenna;
}
