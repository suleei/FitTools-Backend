package com.oft.fittools.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressDTO {
    @NotBlank(message = "行政区不能为空")
    @Size(max = 50,message = "行政区字段长度最大50字符")
    String district;
    @NotBlank(message = "地址不能为空")
    @Size(max = 50,message = "地址字段长度最大50字符")
    String address;
    @NotBlank(message = "地点不能为空")
    @Size(max = 50,message = "地址字段长度最大50字符")
    String name;
    @NotBlank(message = "经度不能为空")
    @Size(max = 50,message = "经度字段长度最大10字符")
    String longitude;
    @NotBlank(message = "纬度不能为空")
    @Size(max = 50,message = "纬度字段长度最大10字符")
    String latitude;

    public AddressDTO() {}
    public AddressDTO(String district, String address, String name) {
        this.district = district;
        this.address = address;
        this.name = name;
    }
}
