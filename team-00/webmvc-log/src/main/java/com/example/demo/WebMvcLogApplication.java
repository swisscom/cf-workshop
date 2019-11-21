package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WebMvcLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebMvcLogApplication.class, args);
    }

}
