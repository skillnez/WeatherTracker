package com.skillnez.weathertracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class LocationResponseDto {

    private String name;

    @JsonProperty("local_names")
    private Map<String, String> localNames;

    private BigDecimal lat;

    private BigDecimal lon;

    private String country;

    private String state;


}
