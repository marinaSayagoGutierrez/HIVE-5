package com.hive5.conectivitysecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.hive5.conectivitysecurity.config.RSAKeyRecord;

@SpringBootApplication
@EnableConfigurationProperties(RSAKeyRecord.class)
public class ConectivitySecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConectivitySecurityApplication.class, args);
	}

}
