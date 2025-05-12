package com.skillnez.weathertracker.service;

import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    public String getCurrent() {
        return "Ясно, 23°C";
    }
}
