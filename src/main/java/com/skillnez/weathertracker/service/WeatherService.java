package com.skillnez.weathertracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skillnez.weathertracker.dto.LocationResponseDto;
import com.skillnez.weathertracker.dto.WeatherApiResponseDto;
import com.skillnez.weathertracker.utils.JsonToDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class WeatherService {

    @Value("${openweather.api.key}")
    private String apiKey;

    private final WebClient webClient;

    @Autowired
    public WeatherService(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<LocationResponseDto> getGeoByCityName(String city) {
        String response = webClient.get()
                .uri("https://api.openweathermap.org/geo/1.0/direct?q={city name}&limit={limit}&appid={API key}",
                        city, 5, apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        try {
            List<LocationResponseDto> locationResponseDto = JsonToDtoMapper.mapToLocationResponseDto(response);
            int i = 123;
            return locationResponseDto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }




}
