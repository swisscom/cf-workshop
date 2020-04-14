package com.example.demo.services;

import com.example.demo.adapters.ConsumerAdapter;
import com.example.demo.adapters.WeatherDataAdapter;
import com.example.demo.entities.WeatherData;
import com.example.demo.entities.openweathermap.Coord;
import com.example.demo.entities.openweathermap.Main;
import com.example.demo.entities.openweathermap.OpenWeatherMapResponse;
import com.example.demo.entities.openweathermap.Rain;
import com.example.demo.entities.openweathermap.Snow;
import com.example.demo.entities.openweathermap.Sys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherDataService {

    private final WeatherDataAdapter weatherDataAdapter;
    private final ConsumerAdapter consumerAdapter;
    private final Environment environment;

    public void updateWeatherData() {
        String location = environment.getProperty("LOCATION");
        if (StringUtils.isNotBlank(location)) {
            Flux.fromArray(location.split("\\|"))
                    .concatMap(this::getWeatherData)
                    .concatMap(consumerAdapter::send)
                    .subscribe();
        }
    }

    public Mono<WeatherData> getWeatherData(String location) {
        return weatherDataAdapter.getWeatherData(location)
                .flatMap(this::mapToWeatherData);
    }

    private Mono<WeatherData> mapToWeatherData(OpenWeatherMapResponse response) {
        Coord coord = response.getCoord();
        Main main = response.getMain();
        Sys sys = response.getSys();

        if (coord != null && main != null && sys != null) {
            Rain rain = response.getRain();
            Snow snow = response.getSnow();
            WeatherData weatherData = new WeatherData();
            weatherData.setCelsius(main.getTemp());
            weatherData.setHumidity(main.getHumidity());
            weatherData.setRain(rain != null ? rain.getOneHour() : null);
            weatherData.setSnow(snow != null ? snow.getOneHour() : null);
            weatherData.setLocation(response.getName());
            weatherData.setCountry(sys.getCountry());
            weatherData.setCoordinatesLatitude(coord.getLat());
            weatherData.setCoordinatesLongitude(coord.getLon());
            weatherData.setLocalTime(getLocalTime(response));
            return Mono.just(weatherData);
        } else {
            log.warn("Required Attribute 'coord' or 'main' is missing: coord={}, main={}", coord, main);
            return Mono.empty();
        }
    }

    private String getLocalTime(OpenWeatherMapResponse response) {
        Long dateTime = response.getDt();
        Integer timezone = response.getTimezone();
        if (dateTime != null && timezone != null) {
            OffsetDateTime offsetDateTime = Instant.ofEpochSecond(dateTime).atOffset(ZoneOffset.ofTotalSeconds(timezone));
            return offsetDateTime.toString();
        } else {
            return null;
        }
    }
}
