package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("vcap.services.openweathermap.credentials")
public class ApiProperties {

    private String appId;
    private String baseUrl = "https://api.openweathermap.org/data/2.5/weather";
}
