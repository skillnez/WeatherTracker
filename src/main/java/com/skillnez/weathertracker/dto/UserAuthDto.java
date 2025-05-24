package com.skillnez.weathertracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAuthDto {
    @NotBlank(message = "Account name can't be blank")
    @Size(min = 4, max = 32, message = "Account name must be at least 4 and no more than 32 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9_]+$",
            message = "Username can contain only Latin letters and digits"
    )
    private String username;

    @NotBlank(message = "Password can't be blank")
    @Size(min = 4, max = 64, message = "Password must be at least 4 and no more than 64 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-={}|\\[\\]:;\"'<>,.?/~`]+$",
            message = "Password can contain only letters, digits and special characters (no spaces)"
    )
    private String password;

    private String repeatPassword;
}
