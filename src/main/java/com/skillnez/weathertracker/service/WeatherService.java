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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    private final WebClient webClient;

    private final UserRepository userRepository;

    private static final int CONNECTION_TIMEOUT_SECONDS = 20;


    @Autowired
    public WeatherService(WebClient webClient, UserRepository userRepository) {
        this.webClient = webClient;
        this.userRepository = userRepository;

    }

    public List<LocationResponseDto> getGeoByCityName(String city) {
        String response = getApiResponse(apiRequestByName, city, requestLocationsCountLimit, apiKey);
        try {
            return JsonToDtoMapper.mapToLocationResponseDto(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }

    public WeatherApiResponseDto getWeatherByCoordinates(BigDecimal lat, BigDecimal lon) {
        String response = getApiResponse(apiRequestByCoordinates,
                lat, lon, apiKey);
        try {
            return JsonToDtoMapper.mapToWeatherApiResponseDto(response);
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

    private String getApiResponse (String requestUri, Object... uriVariables) {
        return webClient.get()
                .uri(requestUri, uriVariables)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(body -> {
                                    log.error("API error. Status: {}, Body: {}", clientResponse.statusCode(), body);
                                    return new ApiException("Api response error has occurred " + body);
                                })
                )
                .bodyToMono(String.class)
                .doOnSubscribe(subscription ->
                        log.info("Calling external API: {}", requestUri)
                )
                .doOnSuccess(body ->
                        log.debug("Received response from {}: {}", requestUri, body)
                )
                .doOnError(e ->
                        log.error("Error during API call to {}: {}", requestUri, e.getMessage(), e)
                )
                .onErrorResume(e -> Mono.error(new ApiException("Unexpected api response error has occurred")))
                .timeout(Duration.ofSeconds(CONNECTION_TIMEOUT_SECONDS))
                .log()
                .block();
    }
}