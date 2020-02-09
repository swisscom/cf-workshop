package com.example.demo;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class WebfluxTask {

    public static void main(String[] args) {
        new SpringApplicationBuilder(WebfluxTask.class)
                .web(WebApplicationType.NONE)
                .run();
    }

}
