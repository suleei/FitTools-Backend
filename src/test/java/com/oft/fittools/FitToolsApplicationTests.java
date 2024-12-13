package com.oft.fittools;

import com.oft.fittools.mapper.UserBasicInfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class FitToolsApplicationTests {
	@Autowired
	UserBasicInfoMapper userBasicInfoMapper;

	@Autowired
	StringRedisTemplate redisTemplate;

	@Autowired
	PasswordEncoder passwordEncoder;
	@Test
	void contextLoads() {
	}
	@Test
	void mybatisTest(){
		System.out.println(userBasicInfoMapper.getUserBasicInfoByUsername("suleei").getUsername());
	}

	@Test
	void passwordEncoder(){
		System.out.println(passwordEncoder.encode("1234"));
	}

	@Test
	void redisTest(){
		redisTemplate.opsForHash().put("test","one","two");
		System.out.println(redisTemplate.opsForValue().get("EMailIdentifyingCode:suleei@qq.com"));
	}

	@Test
	void rocketMqTest(){

	}
}
