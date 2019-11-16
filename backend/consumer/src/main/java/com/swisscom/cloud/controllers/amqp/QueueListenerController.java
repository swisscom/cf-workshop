package com.swisscom.cloud.controllers.amqp;

import com.swisscom.cloud.entities.WeatherData;
import com.swisscom.cloud.services.WeatherDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QueueListenerController {

    private final WeatherDataService weatherDataService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "weather-data", durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = "amq.direct", durable = "true", autoDelete = "false")
    ))
    public void processMessage(WeatherData weatherData) {
        weatherDataService.logWeatherData(weatherData);
    }

}