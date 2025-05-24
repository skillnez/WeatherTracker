package com.skillnez.weathertracker.utils;

import com.skillnez.weathertracker.dto.UserRegistrationDto;
import com.skillnez.weathertracker.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegistrationValidator implements Validator {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationValidator(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegistrationDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegistrationDto user = (UserRegistrationDto) target;

        if (!user.getPassword().equals(user.getRepeatPassword())){
            errors.rejectValue("repeatPassword","repeatPassword.invalid", "Passwords don't match");
        }
        if (registrationService.isUserAlreadyRegistered(user)){
            errors.rejectValue("username","username.invalid", "Username is already in use");
        }
    }
}
