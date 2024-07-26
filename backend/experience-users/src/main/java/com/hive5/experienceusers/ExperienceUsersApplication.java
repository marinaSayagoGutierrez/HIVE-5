package com.hive5.experienceusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //(scanBasePackages = {"com.hive5.utils", "com.hive5.experienceusers"})
public class ExperienceUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExperienceUsersApplication.class, args);
	}

}
