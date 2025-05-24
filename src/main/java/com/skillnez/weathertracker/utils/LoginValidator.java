package com.skillnez.weathertracker.utils;

import com.skillnez.weathertracker.dto.UserAuthDto;
import com.skillnez.weathertracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LoginValidator implements Validator {

    private final UserService userService;

    @Autowired
    public LoginValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserAuthDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserAuthDto user = (UserAuthDto) target;

        if (!userService.isUserAlreadyRegistered(user)){
            errors.rejectValue("username","username.invalid", "User with this username don't exist");
        }
    }

}
