package com.oft.fittools.service.impl;

import com.oft.fittools.dto.ham.NearByHamDTO;
import com.oft.fittools.global.UserContextHolder;
import com.oft.fittools.mapper.LocationMapper;
import com.oft.fittools.po.Location;
import com.oft.fittools.po.User;
import com.oft.fittools.service.NearByHamService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoLocation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NearByHamServiceImpl implements NearByHamService {
    private final StringRedisTemplate stringRedisTemplate;
    private final String prefix = "ham_nearby:";
    private final LocationMapper locationMapper;

    @Override
    public boolean getActiveStatus() {
        User user = UserContextHolder.getUser();
        if(StringUtils.isBlank(user.getCall_sign())) return false;
        if(stringRedisTemplate.opsForGeo().position(prefix+"geo", user.getCall_sign()).get(0)==null){
            return false;
        }
        return true;
    }

    @Override
    public void setStatusActive(){
        User user = UserContextHolder.getUser();
        if(StringUtils.isBlank(user.getCall_sign())) throw new RuntimeException("用户未设置呼号");
        if(user.getLocation_id()==null) throw new RuntimeException("用户未设置地理位置");
        Location location = locationMapper.select(user.getLocation_id());
        stringRedisTemplate.opsForGeo().add(prefix+"geo", new Point(Double.parseDouble(location.getLongitude()), Double.parseDouble(location.getLatitude())),user.getCall_sign());
    }

    @Override
    public void setStatusInactive(){
        User user = UserContextHolder.getUser();
        if(StringUtils.isBlank(user.getCall_sign())) throw new RuntimeException("用户未设置呼号");
        stringRedisTemplate.opsForGeo().remove(prefix+"geo", user.getCall_sign());
    }

    @Override
    public List<NearByHamDTO> getNearByHam(Integer distance) {
        User user = UserContextHolder.getUser();
        if (StringUtils.isBlank(user.getCall_sign())) throw new RuntimeException("用户未设置呼号");
        Point point = stringRedisTemplate.opsForGeo().position(prefix + "geo", user.getCall_sign()).get(0);
        Circle circle = new Circle(point.getX(), point.getY(), distance*1000);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending();
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> locations = stringRedisTemplate.opsForGeo().radius(prefix + "geo", circle, args).getContent();
        List<NearByHamDTO> result = new ArrayList<>();
        System.out.println(locations);
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> location : locations) {
            /*if(location.getContent().getName().equals(user.getCall_sign())) continue;*/
            RedisGeoCommands.GeoLocation<String> loc = location.getContent();
            result.add(new NearByHamDTO(String.format("%.6f",loc.getPoint().getX()),String.format("%.6f",loc.getPoint().getY()),String.format("%.3f",location.getDistance().getValue()/1000),loc.getName()));
        }
        return result;
    }
}
