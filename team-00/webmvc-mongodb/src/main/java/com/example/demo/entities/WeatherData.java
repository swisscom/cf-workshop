package com.example.demo.entities;

import lombok.Data;

@Data
public class WeatherData {

    private String location;
    private Double celsius;
    private Double humidity;
    private Double rain;
    private Double snow;
    private Double coordinatesLatitude;
    private Double coordinatesLongitude;
}
