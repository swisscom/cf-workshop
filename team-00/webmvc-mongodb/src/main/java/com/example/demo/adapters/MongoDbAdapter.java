package com.example.demo.adapters;

import com.example.demo.entities.WeatherData;
import com.example.demo.repositories.WeatherDataRepository;
import com.example.demo.repositories.entities.PersistedWeatherData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongoDbAdapter {

    private final WeatherDataRepository weatherDataRepository;

    public void store(WeatherData weatherData) {
        PersistedWeatherData persistedWeatherData = new PersistedWeatherData();
        persistedWeatherData.setLocation(weatherData.getLocation());
        persistedWeatherData.setCelsius(weatherData.getCelsius());
        persistedWeatherData.setHumidity(weatherData.getHumidity());
        persistedWeatherData.setRain(weatherData.getRain());
        persistedWeatherData.setSnow(weatherData.getSnow());
        persistedWeatherData.setCoordinatesLatitude(weatherData.getCoordinatesLatitude());
        persistedWeatherData.setCoordinatesLongitude(weatherData.getCoordinatesLongitude());
        persistedWeatherData.setCreatedAt(Instant.now());
        persistedWeatherData.setDeleteAt(Instant.now().plus(Duration.ofMinutes(10)));
        weatherDataRepository.save(persistedWeatherData);
    }

    public List<PersistedWeatherData> findLastWeatherData() {
        return weatherDataRepository.findByOrderByDeleteAtDesc();
    }
}
