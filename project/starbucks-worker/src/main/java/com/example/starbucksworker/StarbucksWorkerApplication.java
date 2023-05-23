package com.example.starbucksworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableScheduling
public class StarbucksWorkerApplication {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    public static void main(String[] args) {
        SpringApplication.run(StarbucksWorkerApplication.class, args);
    }

}
