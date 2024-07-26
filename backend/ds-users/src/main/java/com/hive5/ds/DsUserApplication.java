package com.hive5.ds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //(scanBasePackages = {"com.hive5.utils", "com.hive5.ds"})
public class DsUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(DsUserApplication.class, args);
	}

}
