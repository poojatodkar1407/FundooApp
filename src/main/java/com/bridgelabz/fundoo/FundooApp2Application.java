package com.bridgelabz.fundoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;


@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
@EnableScheduling
public class FundooApp2Application {

	public static void main(String[] args) {
		System.out.println("FundooApp2Application.main()");
		SpringApplication.run(FundooApp2Application.class, args);
	}

}
