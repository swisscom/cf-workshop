package com.example.demo.controllers;

import com.example.demo.repositories.entities.PersistedWeatherData;
import com.example.demo.services.WeatherDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WeatherDataController {

    private final WeatherDataService weatherDataService;

    @GetMapping
    public List<PersistedWeatherData> getData() {
        return weatherDataService.getLastWeatherData();
    }
}
