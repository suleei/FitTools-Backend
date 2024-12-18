package com.oft.fittools.mapper;

import com.oft.fittools.po.Location;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LocationMapper {
    public void insert(Location location);

    public void delete(int id);

    public Location select(int id);
}
