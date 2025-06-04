package com.skillnez.weathertracker.service;

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

    @Value("${session.lifetime.seconds}")
    private int lifetimeSeconds;

    @Transactional
    protected Session save(User user) {
        return sessionRepository.save(
                Session.builder()
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(lifetimeSeconds))
                .build());
    }

    @Transactional
    public Optional<Session> findById(UUID id) {
        return sessionRepository.findById(id);
    }

    @Transactional
    public void deleteSessionById(UUID id) {
        User user = getUserBySessionId(id);
        user.getSessions().removeIf(session -> session.getId().equals(id));
        userRepository.update(user);
    }

    public User getUserBySessionId (UUID id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Session not found"));
        return userRepository.findById(session.getUser().getId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Transactional
    public void logout(UUID id, String Username) {
        User user = userRepository.findByUsername(Username).orElseThrow(() -> new NoSuchElementException("User not found"));
        user.getSessions().removeIf(session -> session.getId().equals(id));
        userRepository.update(user);
    }

    public boolean isSessionExpired(Session session) {
        return session.getExpiresAt().isBefore(LocalDateTime.now());
    }

    @Transactional
    public void cleanExpiredSessions() {
        sessionRepository.deleteExpiredSessions();
    }
}
