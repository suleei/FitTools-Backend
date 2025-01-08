package com.oft.fittools.dto.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class GetActivitiesReqDTO {
    Date startTime;
    Integer id;
}
