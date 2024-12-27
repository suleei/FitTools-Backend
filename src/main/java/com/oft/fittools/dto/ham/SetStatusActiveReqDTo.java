package com.oft.fittools.dto.ham;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SetStatusActiveReqDTo {

    @NotNull
    @Size(min=7,max=7,message = "时间数组长度必须为7")
    Integer[] times;
}
