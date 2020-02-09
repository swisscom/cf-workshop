package com.example.demo.entities.openweathermap;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Main {

    private Double humidity;
    private Double pressure;
    private Double temp;
    @JsonProperty("temp_max")
    private Double tempMax;
    @JsonProperty("temp_min")
    private Double tempMin;

}
