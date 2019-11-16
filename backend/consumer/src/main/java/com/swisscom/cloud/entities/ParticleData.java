
package com.swisscom.cloud.entities;

import lombok.Data;

import java.time.Instant;

@Data
public class ParticleData {

    private Instant publishedAt;
    private String assetId;
    private WeatherData value;

}
