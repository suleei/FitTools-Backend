package com.oft.fittools;

import com.oft.fittools.mapper.LocationMapper;
import com.oft.fittools.po.Location;
import com.oft.fittools.service.CallSignBloomFilterService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class FitToolsApplicationTests {
    @Autowired
    private CallSignBloomFilterService callSignBloomFilterService;
}
