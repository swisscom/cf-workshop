package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("vcap.services.openweathermap.credentials")
public class ConsumerProperties {

    private String baseUrl = "https://weather-data-consumer-docker.scapp.io/api/weather-data";

}
