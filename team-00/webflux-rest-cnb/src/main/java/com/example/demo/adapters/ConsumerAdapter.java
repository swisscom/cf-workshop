package com.example.demo.adapters;

import com.example.demo.entities.WeatherData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ConsumerAdapter {

    private final WebClient webClient;

    public ConsumerAdapter(Environment environment, WebClient.Builder webclientBuilder) {
        String baseUrl = environment.getProperty("ENDPOINT");
        if (StringUtils.isBlank(baseUrl)) {
            baseUrl = "http://localhost";
        } else {
            log.info("Set target to endpoint={}", baseUrl);
        }
        this.webClient = webclientBuilder.baseUrl(baseUrl).build();
    }

    public Mono<ClientResponse> send(WeatherData weatherData) {

        return webClient.post()
                .uri("api/weather-data")
                .bodyValue(weatherData)
                .exchange()
                .map(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        Mono<String> stringMono = clientResponse.bodyToMono(String.class);
                        log.info("Successfully sent weather-data to REST endpoint");
                    } else {
                        log.warn("Sending weather-data to REST endpoint didn't work as expected, status={}", clientResponse.statusCode());
                    }
                    return clientResponse;
                });

    }
}
