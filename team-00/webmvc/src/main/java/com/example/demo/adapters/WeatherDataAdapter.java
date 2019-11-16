package com.example.demo.adapters;

import com.example.demo.config.ApiProperties;
import com.example.demo.entities.openweathermap.OpenWeatherMapResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Slf4j
@Service
public class WeatherDataAdapter {

    private final ApiProperties apiProperties;
    private final RestTemplate restTemplate;

    public WeatherDataAdapter(ApiProperties apiProperties, RestTemplateBuilder restTemplateBuilder) {
        this.apiProperties = apiProperties;
        this.restTemplate = restTemplateBuilder.build();
    }

    public Optional<OpenWeatherMapResponse> getWeatherData(String searchQuery) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiProperties.getBaseUrl())
                .queryParam("q", searchQuery)
                .queryParam("APPID", apiProperties.getAppId())
                .queryParam("units", "metric");

        ResponseEntity<OpenWeatherMapResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), OpenWeatherMapResponse.class);

        log.info("Received Status: {} for Location: {}", responseEntity.getStatusCode(), searchQuery);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(responseEntity.getBody());
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The API's response was not OK: " + responseEntity.getStatusCode());
        }
    }

}
