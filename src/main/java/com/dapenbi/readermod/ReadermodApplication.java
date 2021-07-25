package com.dapenbi.readermod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReadermodApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReadermodApplication.class, args);
	}

}
