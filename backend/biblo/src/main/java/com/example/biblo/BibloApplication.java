package com.example.biblo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BibloApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibloApplication.class, args);
	}

}
