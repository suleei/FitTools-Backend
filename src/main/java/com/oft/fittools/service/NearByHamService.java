package com.oft.fittools.service;

import com.oft.fittools.dto.ham.NearByHamDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface NearByHamService {

    boolean getActiveStatus();

    void setStatusActive();

    void setStatusInactive();

    List<NearByHamDTO> getNearByHam(@NotNull(message = "距离不能为空") @Min(value = 5, message = "最小为五公里") @Max(value = 6000, message = "最大为六千公里") Integer distance);
}
