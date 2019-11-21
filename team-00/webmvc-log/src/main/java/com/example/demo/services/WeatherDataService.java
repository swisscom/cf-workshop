package com.example.demo.services;

import com.example.demo.adapters.WeatherDataAdapter;
import com.example.demo.entities.WeatherData;
import com.example.demo.entities.openweathermap.Coord;
import com.example.demo.entities.openweathermap.Main;
import com.example.demo.entities.openweathermap.OpenWeatherMapResponse;
import com.example.demo.entities.openweathermap.Rain;
import com.example.demo.entities.openweathermap.Snow;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class WeatherDataService {

    private final WeatherDataAdapter weatherDataAdapter;
    private final String location;

    public WeatherDataService(WeatherDataAdapter weatherDataAdapter, Environment environment) {
        this.weatherDataAdapter = weatherDataAdapter;

        if (StringUtils.isNotBlank(environment.getProperty("LOCATION"))) {
            this.location = environment.getProperty("LOCATION");
        } else {
            this.location = "Worblaufen,ch";
            log.warn("Environment-variable 'LOCATION' not set. Using default: {}", location);
        }

    }

    public void updateWeatherData() {
        getWeatherData(this.location)
                .ifPresent(this::logWeatherData);
    }

    private void logWeatherData(WeatherData weatherData) {
        log.info("Received WeatherData for location={}: celsius={}, humidity={}, coordinates={},{}",
                weatherData.getLocation(), weatherData.getCelsius(), weatherData.getHumidity(), weatherData.getCoordinatesLatitude(), weatherData.getCoordinatesLongitude());
    }

    public Optional<WeatherData> getWeatherData(String location) {
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
}