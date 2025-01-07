package com.oft.fittools.dto.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
public class CommunicationLogDTO {
    Integer id;
    @NotBlank(message = "己方呼号不能为空")
    @Size(max=10,message = "己方呼号字符长度最长为10")
    String source_call_sign;
    @NotBlank(message = "对方呼号不能为空")
    @Size(max=10,message = "对方呼号字符长度最长为10")
    String target_call_sign;
    @NotBlank(message = "己方地址不能为空")
    @Size(max=100,message = "己方地址字符长度最长为100")
    String source_address;
    @NotBlank(message = "己方政区不能为空")
    @Size(max=100,message = "己方政区字符长度最长为100")
    String source_district;
    @NotNull(message = "己方经度不能为空")
    Double source_lng;
    @NotNull(message = "己方纬度不能为空")
    Double source_lat;
    @NotBlank(message = "对方地址不能为空")
    @Size(max=100,message = "对方地址字符长度最长为100")
    String target_address;
    @NotNull(message = "对方纬度不能为空")
    Double target_lng;
    @NotNull(message = "对方纬度不能为空")
    Double target_lat;
    @NotBlank(message = "己方设备不能为空")
    @Size(max=50,message = "己方设备字符长度最长为50")
    String source_device;
    @NotBlank(message = "对方设备不能为空")
    @Size(max=50,message = "对方设备字符长度最长为50")
    String target_device;
    @NotBlank(message = "己方天线不能为空")
    @Size(max=50,message = "己方天线字符长度最长为50")
    String source_antenna;
    @NotBlank(message = "对方天线不能为空")
    @Size(max=50,message = "对方天线字符长度最长为50")
    String target_antenna;
    @NotNull(message = "己方功率不能为空")
    Double source_power;
    @NotNull(message = "对方功率不能为空")
    Double target_power;
    @NotNull(message = "频率不能为空")
    Double frequency;
    @NotBlank(message = "模式不能为空")
    @Size(max=10,message = "模式字符长度最长为10")
    String mode;
    @NotBlank(message = "己方信号报告不能为空")
    @Size(min=2,max=3,message = "己方信号报告长度为2-3字符")
    String source_rst;
    @NotBlank(message = "对方信号报告不能为空")
    @Size(min=2,max=3,message = "对方信号报告长度为2-3字符")
    String target_rst;
    @NotBlank(message = "天气不能为空")
    @Size(max=100,message = "天气字符长度最长为100")
    String weather;
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date start_time;
    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date end_time;
    String comments;
    Character confirm_status;
    @NotNull
    Double distance;
    Long duration;

    @NotNull(message = "指定是否发布活动不能为空")
    Boolean publish_activity;
}
