package com.example.demo.adapters;

import com.example.demo.entities.WeatherData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMqAdapter {

    private static final String ROUTING_KEY = "weather-data";
    private final RabbitTemplate rabbitTemplate;
    private final CachingConnectionFactory cachingConnectionFactory;

    public void send(WeatherData weatherData) {
        rabbitTemplate.convertAndSend(ROUTING_KEY, weatherData);
        log.info("Stored Message of {} in RabbitMQ", weatherData.getLocation());
    }

    public void send(List<WeatherData> weatherDataList) {
        weatherDataList.forEach(this::send);
        cachingConnectionFactory.resetConnection();
    }
}
