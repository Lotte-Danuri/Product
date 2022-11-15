package com.lotte.danuri.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class ProductApplication {

	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}

}
