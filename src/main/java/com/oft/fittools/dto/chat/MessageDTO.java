package com.oft.fittools.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class MessageDTO {
    Date time;
    String source;
    String target;
    String message;
}
