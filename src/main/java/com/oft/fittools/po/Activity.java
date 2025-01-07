package com.oft.fittools.po;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class Activity {
    Integer id;
    final Integer log_id;
    final Date start_time;
    final Integer publisher;
    final String source;
    final String target;
    final Double distance;

}
