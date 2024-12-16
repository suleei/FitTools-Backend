package com.oft.fittools.service.impl;

import cn.hutool.Hutool;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.oft.fittools.dto.user.GetUserInfoRespDTO;
import com.oft.fittools.mapper.UserMapper;
import com.oft.fittools.po.User;
import com.oft.fittools.service.UserService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final MinioClient minioClient;
    private final UserMapper userMapper;
    private Snowflake snowflake = IdUtil.createSnowflake(1,1);

    @Value("${minio.endpoint}")
    private String endpoint;

    @Override
    public void uploadAvatar(MultipartFile file) {
        if(!file.getContentType().startsWith("image")) throw new RuntimeException("上传的用户头像必须为图片");
        StringBuilder sb=new StringBuilder();
        sb.append(Long.toHexString(snowflake.nextId()));
        sb.append(".");
        sb.append(file.getContentType().split("/")[1]);
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("avatar")
                            .object(sb.toString())
                            .stream(file.getInputStream(), file.getInputStream().available(), -1)
                            .build()
            );
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        User user = userMapper.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        userMapper.updateAvatar(SecurityContextHolder.getContext().getAuthentication().getName(), sb.toString());
        if(user.getAvatar()!=null){
            try {
                minioClient.removeObject(
                        RemoveObjectArgs
                                .builder()
                                .bucket("avatar")
                                .object(user.getAvatar())
                                .build()
                );
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public GetUserInfoRespDTO getUserInfo() {
        User user = userMapper.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        GetUserInfoRespDTO dto = new GetUserInfoRespDTO();
        BeanUtils.copyProperties(user,dto);
        String avatarURL = endpoint + "/avatar/";
        if(user.getAvatar()!=null)
            avatarURL += user.getAvatar();
        else
            avatarURL += "avatar.png";
        dto.setAvatar(avatarURL);
        return dto;
    }
}
