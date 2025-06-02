package com.skillnez.weathertracker.controller;

import com.skillnez.weathertracker.dto.UserAuthDto;
import com.skillnez.weathertracker.exception.UserAlreadyExistsException;
import com.skillnez.weathertracker.service.registration.RegisterFacadeService;
import com.skillnez.weathertracker.utils.RegistrationValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        if (!model.containsAttribute("userAuthDto")) {
            model.addAttribute("userAuthDto", new UserAuthDto());
        }
        return "sign-up-with-errors";
    }

    @PostMapping("/register")
    public String register (@ModelAttribute @Valid UserAuthDto userAuthDto, BindingResult bindingResult) {
        registrationValidator.validate(userAuthDto, bindingResult);
        if (bindingResult.hasErrors()) {
            //Интересный факт для себя на будущее
            //(Поля Type=Password в HTML сбросятся автоматом, даже если они сохранены в модели)
            return "sign-up-with-errors";
        }
        registerFacadeService.registerUser(userAuthDto);
        return "redirect:/login";
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExistsException(UserAlreadyExistsException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("registerError", e.getMessage());
        redirectAttributes.addFlashAttribute("userAuthDto", new UserAuthDto());
        return "redirect:/register";
    }

}
