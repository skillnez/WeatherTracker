package com.skillnez.weathertracker.controller;

import com.skillnez.weathertracker.dto.UserAuthDto;
import com.skillnez.weathertracker.service.registration.RegisterFacadeService;
import com.skillnez.weathertracker.utils.RegistrationValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping()
public class RegistrationController {

    private final RegistrationValidator registrationValidator;
    private final RegisterFacadeService registerFacadeService;

    @Autowired
    public RegistrationController(RegistrationValidator registrationValidator, RegisterFacadeService registerFacadeService) {
        this.registrationValidator = registrationValidator;
        this.registerFacadeService = registerFacadeService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userAuthDto", new UserAuthDto());
        model.addAttribute("registerError", null);
        return "sign-up-with-errors";
    }

    @PostMapping("/register")
    public String register (@ModelAttribute @Valid UserAuthDto userAuthDto, BindingResult bindingResult) {
        registrationValidator.validate(userAuthDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "sign-up-with-errors";
        }
        registerFacadeService.registerUser(userAuthDto);
        return "redirect:/login";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e, Model model) {
        model.addAttribute("registerError", e.getMessage());
        model.addAttribute("userAuthDto", new UserAuthDto());
        return "sign-up-with-errors";
    }

}
