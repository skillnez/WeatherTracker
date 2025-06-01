package com.skillnez.weathertracker.controller;

import com.skillnez.weathertracker.dto.UserAuthDto;
import com.skillnez.weathertracker.entity.Session;
import com.skillnez.weathertracker.service.authorization.AuthFacadeService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final AuthFacadeService authFacadeService;

    public LoginController(AuthFacadeService authFacadeService) {
        this.authFacadeService = authFacadeService;
    }

    @Value("${session.lifetime.seconds}")
    private int lifetimeSeconds;

    @GetMapping("/login")
    public String login(Model model) {
        if (!model.containsAttribute("userAuthDto")) {
            model.addAttribute("userAuthDto", new UserAuthDto());
        }
        return "sign-in-with-errors";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute @Valid UserAuthDto userAuthDto,
                            BindingResult bindingResult,
                            HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "sign-in-with-errors";
        }

        Session session = authFacadeService.login(userAuthDto);

        Cookie sessionCookie = new Cookie("session_token", session.getId().toString());
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(lifetimeSeconds);
        response.addCookie(sessionCookie);

        return "redirect:/";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("authError", e.getMessage());
        redirectAttributes.addFlashAttribute("userAuthDto", new UserAuthDto());
        return "redirect:/login";
    }
}
