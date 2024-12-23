package com.oft.fittools.po;

import lombok.Data;

import java.util.Date;

@Data
public class CommunicationLog {
    Integer id;
    Integer user_id;
    String source_call_sign;
    String target_call_sign;
    String source_address;
    String source_district;
    Double source_lng;
    Double source_lat;
    String target_address;
    Double target_lng;
    Double target_lat;
    String source_device;
    String target_device;
    String source_antenna;
    String target_antenna;
    Double source_power;
    Double target_power;
    Double frequency;
    String mode;
    String source_rst;
    String target_rst;
    String weather;
    Date start_time;
    Date end_time;
    Character confirm_status;
    String comments;
    Double distance;
    Long duration;
}
