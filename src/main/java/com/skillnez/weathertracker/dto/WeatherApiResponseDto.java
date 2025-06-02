package com.skillnez.weathertracker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherApiResponseDto {
    private String name;
    private String country;
    private Double temperature;
    private Double feelsLike;
    private Integer humidity;
    private String description;
    private String icon;
}
