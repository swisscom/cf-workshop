package com.example.demo.controllers;

import com.example.demo.services.WeatherDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskController implements CommandLineRunner {

    private final WeatherDataService weatherDataService;

    @Override
    public void run(String... args) {
        weatherDataService.updateWeatherData();
    }

}

