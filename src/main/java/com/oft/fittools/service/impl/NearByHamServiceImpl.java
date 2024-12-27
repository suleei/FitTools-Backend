package com.oft.fittools.service.impl;

import com.oft.fittools.dto.ham.NearByHamDTO;
import com.oft.fittools.dto.ham.SetStatusActiveReqDTo;
import com.oft.fittools.global.UserContextHolder;
import com.oft.fittools.mapper.LocationMapper;
import com.oft.fittools.po.Location;
import com.oft.fittools.po.User;
import com.oft.fittools.service.NearByHamService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
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
    public Integer[] getActiveTime() {
        if(!getActiveStatus()) throw new RuntimeException("功能未激活");
        User user = UserContextHolder.getUser();
        String srcTime = stringRedisTemplate.opsForValue().get(prefix+"time:"+user.getCall_sign());
        return timeDecoder(srcTime);
    }

    @Override
    public void setStatusActive(SetStatusActiveReqDTo setStatusActiveReqDTo) {
        boolean active = false;
        for(Integer i:setStatusActiveReqDTo.getTimes()) {
            if(i!=0) {
                active = true;
                break;
            }
        }
        if(!active) throw new RuntimeException("活跃时间段不能都为空");
        User user = UserContextHolder.getUser();
        if(StringUtils.isBlank(user.getCall_sign())) throw new RuntimeException("用户未设置呼号");
        if(user.getLocation_id()==null) throw new RuntimeException("用户未设置地理位置");
        Location location = locationMapper.select(user.getLocation_id());
        stringRedisTemplate.opsForGeo().add(prefix+"geo", new Point(Double.parseDouble(location.getLongitude()), Double.parseDouble(location.getLatitude())),user.getCall_sign());
        stringRedisTemplate.opsForValue().set(prefix+"time:"+user.getCall_sign(),times2string(setStatusActiveReqDTo.getTimes()));
    }

    @Override
    public void updateAddress() {
        if(getActiveStatus()) throw new RuntimeException("附近功能未激活");
        User user = UserContextHolder.getUser();
        Location location = locationMapper.select(user.getLocation_id());
        stringRedisTemplate.opsForGeo().add(prefix+"geo", new Point(Double.parseDouble(location.getLongitude()), Double.parseDouble(location.getLatitude())),user.getCall_sign());
    }

    @Override
    public void setStatusInactive(){
        User user = UserContextHolder.getUser();
        if(StringUtils.isBlank(user.getCall_sign())) throw new RuntimeException("用户未设置呼号");
        stringRedisTemplate.opsForGeo().remove(prefix+"geo", user.getCall_sign());
        stringRedisTemplate.delete(prefix+"time:"+user.getCall_sign());
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
        String srcTime = stringRedisTemplate.opsForValue().get(prefix+"time:"+user.getCall_sign());
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> location : locations) {
            RedisGeoCommands.GeoLocation<String> loc = location.getContent();
            String dstTime = stringRedisTemplate.opsForValue().get(prefix+"time:"+loc.getName());
            if(!timeMatch(srcTime,dstTime)) continue;
            result.add(new NearByHamDTO(String.format("%.6f",loc.getPoint().getX()),String.format("%.6f",loc.getPoint().getY()),String.format("%.3f",location.getDistance().getValue()/1000),loc.getName()));
        }
        return result;
    }

    private static String times2string(Integer[] times){
        byte[] bytes = new byte[21];
        int byte_index = 0;
        int bit_index = 0;
        for(int i=0;i<7;i++){
            int num = times[i];
            for(int j=0;j<24;j++){
                if((num&(1<<j))==(1<<j)){
                    bytes[byte_index] = (byte) (bytes[byte_index] | (1<<bit_index));
                }
                bit_index++;
                if(bit_index==8){
                    bit_index=0;
                    byte_index++;
                }
            }
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static boolean timeMatch(String src, String dst){
        byte[] srcBytes = Base64.getDecoder().decode(src);
        byte[] dstBytes = Base64.getDecoder().decode(dst);
        for(int i=0;i<21;i++){
            if((srcBytes[i]&dstBytes[i])!=0) return true;
        }
        return false;
    }

    private static Integer[] timeDecoder(String src){
        byte[] bytes = Base64.getDecoder().decode(src);
        Integer[] times = new Integer[7];
        int byte_index = 0;
        int bit_index = 0;
        for(int i=0;i<7;i++){
            int num = times[i];
            for(int j=0;j<24;j++){
                if((bytes[byte_index] & (1<<bit_index))==1){
                    times[i] = times[i] | (1 << j);
                }
                bit_index++;
                if(bit_index==8){
                    bit_index=0;
                    byte_index++;
                }
            }
        }
        return times;
    }
}
