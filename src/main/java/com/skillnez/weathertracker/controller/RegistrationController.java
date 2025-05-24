package com.skillnez.weathertracker.controller;

import com.skillnez.weathertracker.dto.UserAuthDto;
import com.skillnez.weathertracker.service.UserService;
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
    private final UserService userService;

    @Autowired
    public RegistrationController(RegistrationValidator registrationValidator, UserService userService) {
        this.registrationValidator = registrationValidator;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userAuthDto", new UserAuthDto());
        return "sign-up-with-errors";
    }

    @PostMapping("/register")
    public String register (@ModelAttribute @Valid UserAuthDto userAuthDto, BindingResult bindingResult) {
        registrationValidator.validate(userAuthDto, bindingResult);
        if (bindingResult.hasErrors()) {
            bindingResult.reject("invalidForm", "The fields are filled in incorrectly");
            return "sign-up-with-errors";
        }
        userService.registerUser(userAuthDto);
        return "redirect:/login";
    }
}
