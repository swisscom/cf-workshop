package com.swisscom.cloud.controllers.rest;

import com.swisscom.cloud.entities.ParticleData;
import com.swisscom.cloud.entities.WeatherData;
import com.swisscom.cloud.services.WeatherDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.logstash.logback.argument.StructuredArguments.v;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class WeatherDataController {

    private final WeatherDataService weatherDataService;

    @PostMapping("weather-data")
    public void postData(@RequestBody ParticleData body) {

        WeatherData value = body.getValue();
        if (value != null) {
            weatherDataService.logWeatherData(value);
        } else {
            log.info("Received empty Json", v("body", body));
        }
    }
}
