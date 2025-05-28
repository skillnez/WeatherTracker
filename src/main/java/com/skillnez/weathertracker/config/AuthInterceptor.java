package com.skillnez.weathertracker.config;

import com.skillnez.weathertracker.entity.Session;
import com.skillnez.weathertracker.repository.SessionRepository;
import com.skillnez.weathertracker.service.authorization.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;

    @Autowired
    public AuthInterceptor(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sessionId = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("session_token")) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }
        if (sessionId == null) {
            response.sendRedirect("/login");
            return false;
        }

        UUID sessionUuid;
        try {
            sessionUuid = UUID.fromString(sessionId);
        } catch (IllegalArgumentException e) {
            log.info("UUID format in session cookie incorrect");
            response.sendRedirect("/login");
            return false;
        }

        Optional<Session> sessionOpt = sessionService.findById(sessionUuid);
        if (sessionOpt.isPresent()) {
            Session session = sessionOpt.get();
            if (sessionService.isSessionExpired(session)) {
                response.sendRedirect("/login");
                return false;
            }
            request.setAttribute("username", session.getUser().getLogin());
            return true;
        }
        response.sendRedirect("/login");
        return false;
    }
}
