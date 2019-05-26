package com.challenge.carparkubi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class CarparkubiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarparkubiApplication.class, args);
	}

}
