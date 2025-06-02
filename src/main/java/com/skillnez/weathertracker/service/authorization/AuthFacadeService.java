package com.skillnez.weathertracker.service.authorization;

import com.skillnez.weathertracker.dto.UserAuthDto;
import com.skillnez.weathertracker.entity.Session;
import com.skillnez.weathertracker.entity.User;
import org.springframework.stereotype.Service;

@Service
public class AuthFacadeService {

    private final AuthService authService;
    private final SessionService sessionService;

    public AuthFacadeService(AuthService authService, SessionService sessionService) {
        this.authService = authService;
        this.sessionService = sessionService;
    }

    public Session login(UserAuthDto userAuthDto) throws IllegalArgumentException {
        User user = authService.authenticate(userAuthDto)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        return sessionService.save(user);
    }
}
