package com.example.demo.entities.openweathermap;

import lombok.Data;

@Data
public class Sys {

    private String country;
    private Long id;
    private Long sunrise;
    private Long sunset;
    private Long type;

}
