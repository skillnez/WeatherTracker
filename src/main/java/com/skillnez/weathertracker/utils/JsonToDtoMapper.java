package com.skillnez.weathertracker.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillnez.weathertracker.dto.LocationResponseDto;
import com.skillnez.weathertracker.dto.WeatherApiResponseDto;

import java.util.List;

public class JsonToDtoMapper {

    public static WeatherApiResponseDto mapToWeatherApiResponseDto (String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode weatherNode = root.path("weather").get(0);

        return WeatherApiResponseDto.builder()
                .country(root.path("sys").path("country").asText())
                .temperature(root.path("main").path("temp").asDouble())
                .feelsLike(root.path("main").path("feels_like").asDouble())
                .humidity(root.path("main").path("humidity").asInt())
                .description(weatherNode.path("description").asText())
                .icon(weatherNode.path("icon").asText())
                .build();
    }

    public static List<LocationResponseDto> mapToLocationResponseDto (String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<>() {
        });
    }


}
