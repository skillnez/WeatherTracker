package com.skillnez.weathertracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skillnez.weathertracker.dto.LocationResponseDto;
import com.skillnez.weathertracker.dto.WeatherApiResponseDto;
import com.skillnez.weathertracker.dto.WeatherCardResponseDto;
import com.skillnez.weathertracker.entity.Location;
import com.skillnez.weathertracker.entity.User;
import com.skillnez.weathertracker.repository.UserRepository;
import com.skillnez.weathertracker.utils.JsonToDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.city.uri.req}")
    private  String apiRequestByName;

    @Value("${openweather.coordinates.uri.req}")
    private  String apiRequestByCoordinates;

    @Value("${openweather.limit.locations}")
    private int requestLocationsCountLimit;

    private final UserRepository userRepository;

    private final ApiClientService apiClientService;

    @Autowired
    public WeatherService(UserRepository userRepository, ApiClientService apiClientService) {
        this.userRepository = userRepository;
        this.apiClientService = apiClientService;
    }

    public List<LocationResponseDto> getGeoByCityName(String city) {
        String response = apiClientService.getApiResponse(apiRequestByName, city, requestLocationsCountLimit, apiKey);
        try {
            return JsonToDtoMapper.mapToLocationResponseDto(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }

    public WeatherApiResponseDto getWeatherByCoordinates(BigDecimal lat, BigDecimal lon) {
        String response = apiClientService.getApiResponse(apiRequestByCoordinates,
                lat, lon, apiKey);
        try {
            return JsonToDtoMapper.mapToWeatherApiResponseDto(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }

    @Transactional(readOnly = true)
    public List<WeatherCardResponseDto> getUserWeatherCards(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<Location> locations = user.getLocations();
        List<WeatherCardResponseDto> weatherCardResponseDtoList = new ArrayList<>();
        for (Location location : locations) {
            WeatherApiResponseDto weatherApiResponseDto = getWeatherByCoordinates(location.getLatitude(), location.getLongitude());
            WeatherCardResponseDto weatherCardResponseDto = WeatherCardResponseDto.builder()
                    .id(location.getId())
                    .name(location.getName())
                    .country(weatherApiResponseDto.getCountry())
                    .temperature(weatherApiResponseDto.getTemperature())
                    .feelsLike(weatherApiResponseDto.getFeelsLike())
                    .humidity(weatherApiResponseDto.getHumidity())
                    .description(weatherApiResponseDto.getDescription())
                    .icon(weatherApiResponseDto.getIcon())
                    .lon(location.getLongitude())
                    .lat(location.getLatitude())
                    .build();
            weatherCardResponseDtoList.add(weatherCardResponseDto);
        }
        return weatherCardResponseDtoList;
    }
}