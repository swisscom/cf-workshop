package com.example.demo.entities.openweathermap;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Snow {

    @JsonProperty("1h")
    private Double oneHour;

    @JsonProperty("3h")
    private Double threeHours;

}
