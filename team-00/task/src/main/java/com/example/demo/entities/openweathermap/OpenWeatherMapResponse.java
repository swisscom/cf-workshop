package com.example.demo.entities.openweathermap;

import lombok.Data;

import java.util.List;

@Data
public class OpenWeatherMapResponse {

    private String base;
    private Clouds clouds;
    private Long cod;
    private Coord coord;
    private Long dt;
    private Long id;
    private Main main;
    private String name;
    private Rain rain;
    private Snow snow;
    private Sys sys;
    private Integer timezone;
    private Long visibility;
    private List<Weather> weather;
    private Wind wind;

}
