package com.example.demo.services;

import com.example.demo.adapters.MongoDbAdapter;
import com.example.demo.adapters.RabbitMqAdapter;
import com.example.demo.adapters.WeatherDataAdapter;
import com.example.demo.entities.WeatherData;
import com.example.demo.entities.openweathermap.Coord;
import com.example.demo.entities.openweathermap.Main;
import com.example.demo.entities.openweathermap.OpenWeatherMapResponse;
import com.example.demo.entities.openweathermap.Rain;
import com.example.demo.entities.openweathermap.Snow;
import com.example.demo.repositories.entities.PersistedWeatherData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherDataService {

    private final WeatherDataAdapter weatherDataAdapter;
    private final RabbitMqAdapter rabbitMqAdapter;
    private final MongoDbAdapter mongodbAdapter;
    private final Environment environment;

    public void updateWeatherData() {
        getWeatherData(environment.getProperty("LOCATION"))
                .ifPresent(weatherData -> {
                    rabbitMqAdapter.send(weatherData);
                    mongodbAdapter.store(weatherData);
                });
    }

    private Optional<WeatherData> getWeatherData(String location) {
        return weatherDataAdapter.getWeatherData(location)
                .flatMap(this::mapToWeatherData);
    }

    private Optional<WeatherData> mapToWeatherData(OpenWeatherMapResponse response) {
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
            return Optional.of(weatherData);
        } else {
            log.warn("Required Attribute 'coord' or 'main' is missing: coord={}, main={}", coord, main);
            return Optional.empty();
        }
    }

    public List<PersistedWeatherData> getLastWeatherData() {
        return mongodbAdapter.findLastWeatherData();
    }
}