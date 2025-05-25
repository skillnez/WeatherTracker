package com.skillnez.weathertracker.service.authorization;

import com.skillnez.weathertracker.entity.Session;
import com.skillnez.weathertracker.entity.User;
import com.skillnez.weathertracker.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    protected Session save(User user) {
        return sessionRepository.save(
                Session.builder()
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(3))
                .build());
    }

}
