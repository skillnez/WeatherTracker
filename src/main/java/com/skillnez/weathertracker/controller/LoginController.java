package com.skillnez.weathertracker.controller;

import com.skillnez.weathertracker.dto.UserAuthDto;
import com.skillnez.weathertracker.service.UserService;
import com.skillnez.weathertracker.utils.LoginValidator;
import com.skillnez.weathertracker.utils.RegistrationValidator;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final LoginValidator loginValidator;
    private final UserService userService;

    public LoginController(LoginValidator loginValidator, UserService userService) {
        this.loginValidator = loginValidator;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userAuthDto", new UserAuthDto());
        return "sign-in-with-errors";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute @Valid UserAuthDto userAuthDto, BindingResult bindingResult) {
        loginValidator.validate(userAuthDto, bindingResult);
        if (bindingResult.hasErrors()) {
            bindingResult.reject("invalidForm", "The fields are filled in incorrectly");
            return "sign-in-with-errors";
        }

        UserAuthDto userAuthDto1 = userAuthDto;
        int i = 123;

        return "redirect:/";
    }
}
