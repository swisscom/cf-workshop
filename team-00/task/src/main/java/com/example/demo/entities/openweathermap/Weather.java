package com.example.demo.entities.openweathermap;

import lombok.Data;

@Data
public class Weather {

    private String description;
    private String icon;
    private Long id;
    private String main;

}
