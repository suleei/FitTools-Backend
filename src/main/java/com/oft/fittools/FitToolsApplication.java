package com.oft.fittools;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.oft.fittools")
public class FitToolsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitToolsApplication.class, args);
	}

}
