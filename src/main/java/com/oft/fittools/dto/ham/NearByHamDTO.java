package com.oft.fittools.dto.ham;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NearByHamDTO {
    String lng;
    String lat;
    String distance;
    String call_sign;
}
