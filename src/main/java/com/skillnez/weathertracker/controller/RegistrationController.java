package com.skillnez.weathertracker.controller;

import com.skillnez.weathertracker.dto.UserRegistrationDto;
import com.skillnez.weathertracker.service.RegistrationService;
import com.skillnez.weathertracker.utils.RegistrationValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationValidator registrationValidator, RegistrationService registrationService) {
        this.registrationValidator = registrationValidator;
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "sign-up-with-errors";
    }

    @PostMapping("/register")
    public String register (@ModelAttribute @Valid UserRegistrationDto userDto, BindingResult bindingResult) {
        registrationValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            bindingResult.reject("invalidForm", "The fields are filled in incorrectly");
            return "sign-up-with-errors";
        }
        registrationService.registerUser(userDto);
        return "redirect:/login";
    }
}
