package com.skillnez.weathertracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skillnez.weathertracker.dto.LocationResponseDto;
import com.skillnez.weathertracker.dto.WeatherApiResponseDto;
import com.skillnez.weathertracker.entity.Location;
import com.skillnez.weathertracker.entity.User;
import com.skillnez.weathertracker.repository.UserRepository;
import com.skillnez.weathertracker.service.registration.UserService;
import com.skillnez.weathertracker.utils.JsonToDtoMapper;
import org.flywaydb.core.extensibility.LicenseGuard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    @Value("${openweather.api.key}")
    private String apiKey;

    private final WebClient webClient;

    private final UserRepository userRepository;

    @Autowired
    public WeatherService(WebClient webClient, UserRepository userRepository) {
        this.webClient = webClient;
        this.userRepository = userRepository;

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

    @Transactional
    public List<WeatherApiResponseDto> getUserWeather(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<Location> locations = user.getLocations();
        List<WeatherApiResponseDto> weatherApiResponseDtoList = new ArrayList<>();
        for (Location location : locations) {
            String response = webClient.get()
                    .uri("https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&units=metric&appid={API key}"
                            , location.getLatitude(), location.getLongitude(), apiKey)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            try {
                WeatherApiResponseDto weatherApiResponseDto = JsonToDtoMapper.mapToWeatherApiResponseDto(response);
                //Жесткий костыль чтобы имя добавляемой локации совпадало с именем которая будет на главной странице
                weatherApiResponseDto.setName(location.getName());
                weatherApiResponseDtoList.add(weatherApiResponseDto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error parsing JSON", e);
            }
        }
        return weatherApiResponseDtoList;
    }




}
