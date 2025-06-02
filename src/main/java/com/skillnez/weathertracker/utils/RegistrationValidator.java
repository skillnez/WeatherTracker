package com.skillnez.weathertracker.utils;

import com.skillnez.weathertracker.dto.UserAuthDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegistrationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserAuthDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserAuthDto user = (UserAuthDto) target;

        if (!user.getPassword().equals(user.getRepeatPassword())) {
            errors.rejectValue("repeatPassword", "repeatPassword.invalid", "Passwords don't match");
        }
        if (user.getRepeatPassword().isBlank()) {
            errors.rejectValue("repeatPassword", "repeatPassword.invalid", "Repeat password");
        }
    }
}
