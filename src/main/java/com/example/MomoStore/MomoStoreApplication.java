package com.example.MomoStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MomoStoreApplication {
	private static final Logger log= LoggerFactory.getLogger(MomoStoreApplication.class.getName());
	public static void main(String[] args) {

		SpringApplication.run(MomoStoreApplication.class, args);
		log.info("App has started");
	}

}
