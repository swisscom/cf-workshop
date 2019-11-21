package com.example.demo.adapters;

import com.example.demo.entities.WeatherData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMqAdapter {

    private static final String ROUTING_KEY = "weather-data";
    private final RabbitTemplate rabbitTemplate;

    public void send(WeatherData weatherData) {
        rabbitTemplate.convertAndSend(ROUTING_KEY, weatherData);
        log.info("Stored Message in RabbitMQ");
    }
}
