package com.skillnez.weathertracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WeatherCardResponseDto {
    private long id;
    private String name;
    private String country;
    private Double temperature;
    private Double feelsLike;
    private Integer humidity;
    private String description;
    private String icon;
    private BigDecimal lon;
    private BigDecimal lat;
}
