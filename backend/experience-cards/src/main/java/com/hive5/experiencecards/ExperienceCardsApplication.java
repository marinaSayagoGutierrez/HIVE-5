package com.hive5.experiencecards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //(scanBasePackages = {"com.hive5.utils", "com.hive5.experiencecards"})
public class ExperienceCardsApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExperienceCardsApplication.class, args);
	}

}
