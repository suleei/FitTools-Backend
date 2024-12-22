package com.oft.fittools.po;

import lombok.Data;

@Data
public class Device {
    private int id;
    private int user_id;
    private String name;
    private double power;
    private String antenna;
}
