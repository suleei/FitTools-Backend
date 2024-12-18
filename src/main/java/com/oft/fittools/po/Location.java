package com.oft.fittools.po;

import lombok.Data;

@Data
public class Location {
    Integer id;
    String district;
    String address;
    String name;
    String longitude;
    String latitude;
}
