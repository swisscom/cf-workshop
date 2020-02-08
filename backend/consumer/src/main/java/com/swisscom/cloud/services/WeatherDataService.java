package com.swisscom.cloud.services;

import com.swisscom.cloud.entities.WeatherData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static net.logstash.logback.argument.StructuredArguments.v;

@Slf4j
@Service
public class WeatherDataService {

    public void logWeatherData(WeatherData weatherData) {

        Double latitude = weatherData.getCoordinatesLatitude();
        Double longitude = weatherData.getCoordinatesLongitude();

        String coordinates = null;
        if (latitude != null && longitude != null) {
            coordinates = String.format("%s,%s", latitude, longitude);
        }

        log.info("Received WeatherData",
                v("event", "WeatherData"),
                v("location", weatherData.getLocation()),
                v("country", weatherData.getCountry()),
                v("localDateTime", weatherData.getLocalTime()),
                v("coordinates", coordinates),
                v("celsius", weatherData.getCelsius()),
                v("humidity", weatherData.getHumidity()),
                v("rain", weatherData.getRain()),
                v("snow", weatherData.getSnow())
        );

    }
}
