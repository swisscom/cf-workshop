package com.example.demo.adapters;

import com.example.demo.config.ApiProperties;
import com.example.demo.entities.openweathermap.OpenWeatherMapResponse;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
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
    private final String appId;

    public WeatherDataAdapter(ApiProperties apiProperties, RestTemplateBuilder restTemplateBuilder, Environment environment) {
        this.apiProperties = apiProperties;
        this.restTemplate = restTemplateBuilder.build();

        if (StringUtils.isNotBlank(apiProperties.getAppId())) {
            this.appId = apiProperties.getAppId();
        } else if (StringUtils.isNotBlank(environment.getProperty("APPID"))) {
            this.appId = environment.getProperty("APPID");
        } else {
            log.error("Could not find an API-Key for backend-calls. Have you provided a secrets-store or the environment-variable APPID?");
            this.appId = null;
        }
    }

    public Optional<OpenWeatherMapResponse> getWeatherData(String searchQuery) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiProperties.getBaseUrl())
                .queryParam("q", searchQuery)
                .queryParam("APPID", this.appId)
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
