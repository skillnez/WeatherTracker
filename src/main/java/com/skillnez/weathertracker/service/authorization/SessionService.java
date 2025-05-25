package com.skillnez.weathertracker.service.authorization;

import com.skillnez.weathertracker.entity.Session;
import com.skillnez.weathertracker.entity.User;
import com.skillnez.weathertracker.repository.SessionRepository;
import com.skillnez.weathertracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    @Value("${session.lifetime.hours}")
    private int lifetimeHours;

    @Transactional
    protected Session save(User user) {
        return sessionRepository.save(
                Session.builder()
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(lifetimeHours))
                .build());
    }

    @Transactional
    public Optional<Session> findById(UUID id) {
        return sessionRepository.findById(id);
    }
}
