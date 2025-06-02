package com.skillnez.weathertracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SearchFormDto {

    @NotBlank
    @Pattern(
            regexp = "^[а-яА-ЯёЁa-zA-Z0-9., -]*$",
            message = "Только буквы (русские/латинские), цифры, точка и запятая"
    )
    @Size(max = 128, message = "Search query too long. Do not input more than 128 characters")
    private String query;

}
