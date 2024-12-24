package com.oft.fittools.dto.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;


@Data
public class CommunicationLogPageDTO {
    Integer id;
    String target_call_sign;
    String target_address;
    @JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss", timezone = "GMT+8")
    Date start_time;
    Character confirm_status;
    Double distance;
}
