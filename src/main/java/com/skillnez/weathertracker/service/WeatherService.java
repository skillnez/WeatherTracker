package com.skillnez.weathertracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skillnez.weathertracker.dto.LocationResponseDto;
import com.skillnez.weathertracker.dto.WeatherApiResponseDto;
import com.skillnez.weathertracker.dto.WeatherCardResponseDto;
import com.skillnez.weathertracker.entity.Location;
import com.skillnez.weathertracker.entity.User;
import com.skillnez.weathertracker.exception.ApiException;
import com.skillnez.weathertracker.repository.UserRepository;
import com.skillnez.weathertracker.utils.JsonToDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    @Value("${openweather.api.key}")
    private String apiKey;

    private final WebClient webClient;

    private final UserRepository userRepository;

    private static final String GEOCODING_API_REQUEST_BY_NAME =
            "https://api.openweathermap.org/geo/1.0/direct?q={city name}&limit={limit}&appid={API key}";

    private static final String WEATHER_DATA_API_REQUEST_BY_COORDINATES =
            "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&units=metric&appid={API key}";

    private static final int CONNECTION_TIMEOUT_SECONDS = 5;

    @Autowired
    public WeatherService(WebClient webClient, UserRepository userRepository) {
        this.webClient = webClient;
        this.userRepository = userRepository;

    }

    public List<LocationResponseDto> getGeoByCityName(String city) {
        String response = getApiResponse(GEOCODING_API_REQUEST_BY_NAME, city, apiKey);
        try {
            return JsonToDtoMapper.mapToLocationResponseDto(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }

    @Transactional(readOnly = true)
    public List<WeatherCardResponseDto> getUserWeather(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<Location> locations = user.getLocations();
        List<WeatherCardResponseDto> weatherCardResponseDtoList = new ArrayList<>();
        for (Location location : locations) {
            String response = getApiResponse(WEATHER_DATA_API_REQUEST_BY_COORDINATES,
                    location.getLatitude(), location.getLongitude(), apiKey);
            try {
                WeatherApiResponseDto weatherApiResponseDto = JsonToDtoMapper.mapToWeatherApiResponseDto(response);
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
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error parsing JSON", e);
            }
        }
        return weatherCardResponseDtoList;
    }

    private String getApiResponse (String requestUri, Object... uriVariables) {
        return webClient.get()
                .uri(requestUri, uriVariables)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(body -> new ApiException("Api response error has occurred " + body))
                )
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.error(new ApiException("Unexpected api response error has occurred")))
                .timeout(Duration.ofSeconds(CONNECTION_TIMEOUT_SECONDS))
                .block();
    }
}
