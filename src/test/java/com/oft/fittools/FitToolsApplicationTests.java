package com.oft.fittools;

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
    private MinioClient minioClient;

    @Test
    void fileUpload() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        FileInputStream fileInputStream = new FileInputStream(new File("src/test/java/com/oft/fittools/2.png"));
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("avatar")
                        .object("test1.png")
                        .stream(fileInputStream, fileInputStream.available(), -1)
                        .build()
        );
    }
}
