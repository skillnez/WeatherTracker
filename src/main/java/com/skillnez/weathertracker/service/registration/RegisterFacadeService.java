package com.skillnez.weathertracker.service.registration;

import com.skillnez.weathertracker.dto.UserAuthDto;
import com.skillnez.weathertracker.exception.UserAlreadyExistsException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterFacadeService {

    private final UserService userService;

    public RegisterFacadeService(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    public void registerUser(UserAuthDto userAuthDto) throws UserAlreadyExistsException {
        try {
            userService.saveUser(userAuthDto);
        } catch (ConstraintViolationException e) {
            throw new UserAlreadyExistsException("Account with this username " + userAuthDto.getUsername() + " already exists");
        }

    }

}
