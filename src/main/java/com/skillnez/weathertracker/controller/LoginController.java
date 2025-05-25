package com.skillnez.weathertracker.controller;

import com.skillnez.weathertracker.dto.UserAuthDto;
import com.skillnez.weathertracker.service.authorization.AuthFacadeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final AuthFacadeService authFacadeService;

    public LoginController(AuthFacadeService authFacadeService) {
        this.authFacadeService = authFacadeService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userAuthDto", new UserAuthDto());
        model.addAttribute("authError", null);
        return "sign-in-with-errors";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute @Valid UserAuthDto userAuthDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "sign-in-with-errors";
        }
        authFacadeService.login(userAuthDto);

        return "redirect:/";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e, Model model) {
        model.addAttribute("authError", e.getMessage());
        model.addAttribute("userAuthDto", new UserAuthDto());
        return "sign-in-with-errors";
    }
}
