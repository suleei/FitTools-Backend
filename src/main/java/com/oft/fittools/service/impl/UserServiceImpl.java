package com.oft.fittools.service.impl;

import cn.hutool.Hutool;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.oft.fittools.dto.user.EmailModificationReqDTO;
import com.oft.fittools.dto.user.EmailSendingReqDTO;
import com.oft.fittools.dto.user.GetUserInfoRespDTO;
import com.oft.fittools.dto.user.NewEmailSendingReqDTO;
import com.oft.fittools.mapper.UserMapper;
import com.oft.fittools.po.User;
import com.oft.fittools.service.CaptchaService;
import com.oft.fittools.service.MailSendingService;
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

import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final MinioClient minioClient;
    private final UserMapper userMapper;
    private Snowflake snowflake = IdUtil.createSnowflake(1,1);

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${jwt.signatureKey}")
    private String signatureKey;

    private final CaptchaService  captchaService;
    private final MailSendingService mailSendingService;

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

    @Override
    public String updateUsername(String username) {
        userMapper.updateUsername(SecurityContextHolder.getContext().getAuthentication().getName(),username);
        Calendar expireTime = Calendar.getInstance();
        expireTime.add(Calendar.HOUR,24);
        return JWT.create().withClaim("username",username).withExpiresAt(expireTime.getTime()).sign(Algorithm.HMAC256(signatureKey));
    }

    @Override
    public void sendEmailCaptcha(EmailSendingReqDTO emailSendingReqDTO) {
        captchaService.verifyCaptcha(emailSendingReqDTO.getCaptchaHash(), emailSendingReqDTO.getCaptchaCode());
        User user = userMapper.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        mailSendingService.sendCaptcha(user.getEmail(),"修改邮箱");
    }

    @Override
    public String verify(String captcha) {
        User user = userMapper.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(!mailSendingService.verifyCaptcha(user.getEmail(),captcha)) throw new RuntimeException("邮箱验证码错误");
        Calendar expireTime = Calendar.getInstance();
        expireTime.add(Calendar.MINUTE,5);
        String jwt = JWT.create().withClaim("username",user.getUsername()).withClaim("email", user.getEmail()).withExpiresAt(expireTime.getTime()).sign(Algorithm.HMAC256(signatureKey));
        return jwt;
    }

    @Override
    public void sendNewEmailCaptcha(NewEmailSendingReqDTO newEmailSendingReqDTO) {
        captchaService.verifyCaptcha(newEmailSendingReqDTO.getCaptchaHash(), newEmailSendingReqDTO.getCaptchaCode());
        mailSendingService.sendCaptcha(newEmailSendingReqDTO.getEmail(),"邮箱验证");
    }

    @Override
    public void modifyEmail(EmailModificationReqDTO emailModificationReqDTO) {
        if(!mailSendingService.verifyCaptcha(emailModificationReqDTO.getEmail(), emailModificationReqDTO.getCaptcha())) throw new RuntimeException("新邮箱验证码错误");
        User user = userMapper.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        JWTVerifier jwtVerifier= JWT.require(Algorithm.HMAC256(signatureKey)).build();
        DecodedJWT decodedJWT =  jwtVerifier.verify(emailModificationReqDTO.getJwt());
        if(!user.getUsername().equals(decodedJWT.getClaim("username").asString())) throw new RuntimeException("授权用户名不一致");
        if(!user.getEmail().equals(decodedJWT.getClaim("email").asString())) throw new RuntimeException("授权邮箱不一致");
        userMapper.updateEmail(user.getUsername(),emailModificationReqDTO.getEmail());
    }
}
