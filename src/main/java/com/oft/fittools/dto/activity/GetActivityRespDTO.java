package com.oft.fittools.dto.activity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class GetActivityRespDTO {
    Integer id;
    String source;
    String avatar;
    String target;
    String sourceAddress;
    Double sourceLng;
    Double sourceLat;
    String targetAddress;
    Double targetLng;
    Double targetLat;
    Double distance;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    Date startTime;
    Integer thumbsCount;
    Boolean thumbsed;
    Integer commentsCount;
}
