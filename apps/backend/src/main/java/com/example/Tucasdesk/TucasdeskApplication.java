package com.example.Tucasdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The main entry point for the TucasDesk Spring Boot application.
 */
@SpringBootApplication
@EnableScheduling
public class TucasdeskApplication {

	/**
	 * The main method that starts the Spring Boot application.
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(TucasdeskApplication.class, args);
	}

}
