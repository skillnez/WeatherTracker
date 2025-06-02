package com.skillnez.weathertracker.controller;

import com.skillnez.weathertracker.service.authorization.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class LogoutController {

    private final SessionService sessionService;

    @Autowired
    public LogoutController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        UUID sessionUuid = (UUID) request.getAttribute("sessionId");
        sessionService.logout(sessionUuid, username);
        return "redirect:/login";
    }

}
