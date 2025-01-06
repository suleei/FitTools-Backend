package com.oft.fittools.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageNotifyDTO {
    String targetCallSign;
    Long cardinality;
}
