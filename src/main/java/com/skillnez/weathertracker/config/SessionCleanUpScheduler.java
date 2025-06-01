package com.skillnez.weathertracker.config;

import com.skillnez.weathertracker.service.authorization.SessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SessionCleanUpScheduler {

    private final SessionService sessionService;

    public SessionCleanUpScheduler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Scheduled(fixedRateString = "${session.cleanup.millis}")
    public void cleanUpSessions() {
        sessionService.cleanExpiredSessions();
    }
}
