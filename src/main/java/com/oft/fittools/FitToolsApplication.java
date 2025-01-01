package com.oft.fittools;

import com.oft.fittools.configuration.WebSocketConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan("com.oft.fittools.mapper")
public class FitToolsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitToolsApplication.class, args);
	}

}
