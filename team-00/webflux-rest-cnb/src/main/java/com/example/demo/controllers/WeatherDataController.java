package com.example.demo.controllers;

import com.example.demo.entities.WeatherData;
import com.example.demo.services.WeatherDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WeatherDataController {

    private final WeatherDataService weatherDataService;

    @GetMapping
    public Mono<WeatherData> postData(@RequestParam String location) {
        return weatherDataService.getWeatherData(location);
    }
}
