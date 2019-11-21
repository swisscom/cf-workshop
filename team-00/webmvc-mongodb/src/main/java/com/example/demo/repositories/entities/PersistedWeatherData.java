package com.example.demo.repositories.entities;

import com.example.demo.entities.WeatherData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document
@EqualsAndHashCode(callSuper = true)
public class PersistedWeatherData extends WeatherData {

    @Id
    private String id;

    private Instant createdAt;

    @Indexed(expireAfterSeconds = 0)
    private Instant deleteAt;
}
