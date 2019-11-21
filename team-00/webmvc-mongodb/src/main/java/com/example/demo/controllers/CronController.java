package com.example.demo.controllers;

import com.example.demo.services.WeatherDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CronController {

    private final WeatherDataService weatherDataService;

    @Scheduled(fixedRateString = "PT1M")
    private void updateWeatherData() {
        weatherDataService.updateWeatherData();
    }
}
