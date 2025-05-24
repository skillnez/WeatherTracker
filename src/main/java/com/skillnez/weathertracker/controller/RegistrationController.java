package com.skillnez.weathertracker.controller;

import com.skillnez.weathertracker.dto.UserRegistrationDto;
import com.skillnez.weathertracker.utils.RegistrationValidator;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class RegistrationController {

    private final RegistrationValidator registrationValidator;

    public RegistrationController(RegistrationValidator registrationValidator) {
        this.registrationValidator = registrationValidator;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "sign-up-with-errors";
    }

    @PostMapping("/register")
    public String register (@ModelAttribute @Valid UserRegistrationDto user, BindingResult bindingResult, Model model) {
        registrationValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            bindingResult.reject("invalidForm", "The fields are filled in incorrectly");
            return "sign-up-with-errors";
        }

        return "redirect:/login";
    }
}
