package com.example.demo.services;

import com.example.demo.adapters.RabbitMqAdapter;
import com.example.demo.adapters.WeatherDataAdapter;
import com.example.demo.entities.WeatherData;
import com.example.demo.entities.openweathermap.Coord;
import com.example.demo.entities.openweathermap.Main;
import com.example.demo.entities.openweathermap.OpenWeatherMapResponse;
import com.example.demo.entities.openweathermap.Rain;
import com.example.demo.entities.openweathermap.Snow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherDataService {

    private final WeatherDataAdapter weatherDataAdapter;
    private final RabbitMqAdapter rabbitMqAdapter;
    private final Environment environment;

    public void updateWeatherData() {
        getWeatherData(environment.getProperty("LOCATION"))
                .flatMap(rabbitMqAdapter::send)
                .subscribe();
    }

    public Mono<WeatherData> getWeatherData(String location) {
        return weatherDataAdapter.getWeatherData(location)
                .flatMap(this::mapToWeatherData);
    }

    private Mono<WeatherData> mapToWeatherData(OpenWeatherMapResponse response) {
        Coord coord = response.getCoord();
        Main main = response.getMain();

        if (coord != null && main != null) {
            Rain rain = response.getRain();
            Snow snow = response.getSnow();
            WeatherData weatherData = new WeatherData();
            weatherData.setCelsius(main.getTemp());
            weatherData.setHumidity(main.getHumidity());
            weatherData.setRain(rain != null ? rain.getOneHour() : null);
            weatherData.setSnow(snow != null ? snow.getOneHour() : null);
            weatherData.setLocation(response.getName());
            weatherData.setCoordinatesLatitude(coord.getLat());
            weatherData.setCoordinatesLongitude(coord.getLon());
            return Mono.just(weatherData);
        } else {
            log.warn("Required Attribute 'coord' or 'main' is missing: coord={}, main={}", coord, main);
            return Mono.empty();
        }
    }
}
