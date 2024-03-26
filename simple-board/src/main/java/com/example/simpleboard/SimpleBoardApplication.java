package com.example.simpleboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing	// Auditing 기능 활성화
@SpringBootApplication
public class SimpleBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleBoardApplication.class, args);
	}

}
