package com.skillnez.weathertracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skillnez.weathertracker.dto.WeatherApiResponseDto;
import com.skillnez.weathertracker.utils.JsonToDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {

    @Value("${openweather.api.key}")
    private String apiKey;

    private final WebClient webClient;

    @Autowired
    public WeatherService(WebClient webClient) {
        this.webClient = webClient;
    }

    public WeatherApiResponseDto getWeatherByCity(String city) {
        String response = webClient.get()
                .uri("https://api.openweathermap.org/data/2.5/weather?q={city}&units=metric&appid={API key}", city, apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        try {
            WeatherApiResponseDto weatherApiResponseDto = JsonToDtoMapper.mapToWeatherApiResponseDto(response);
            int i = 123;
            return weatherApiResponseDto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }




}
