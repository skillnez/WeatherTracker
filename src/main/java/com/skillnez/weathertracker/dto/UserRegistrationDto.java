package com.skillnez.weathertracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegistrationDto {
    @NotBlank(message = "Account name can't be blank")
    @Size(min = 4, max = 32, message = "Account name must be at least 4 and no more than 32 characters")
    private String username;

    @NotBlank(message = "Password can't be blank")
    @Size(min = 4, max = 64, message = "Password must be at least 4 and no more than 64 characters")
    private String password;

    @NotBlank(message = "Repeat password")
    private String repeatPassword;
}
