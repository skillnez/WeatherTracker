package com.skillnez.weathertracker.controller;

import com.skillnez.weathertracker.dto.WeatherApiResponseDto;
import com.skillnez.weathertracker.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/city")
    public WeatherApiResponseDto city(@RequestParam("city") String city) {
        return weatherService.getWeatherByCity(city);
    }

}
