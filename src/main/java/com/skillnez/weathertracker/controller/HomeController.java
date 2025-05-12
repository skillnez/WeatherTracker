package com.skillnez.weathertracker.controller;

import com.skillnez.weathertracker.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final WeatherService weatherService;

    public HomeController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("weather", weatherService.getCurrent());
        return "index"; // /WEB-INF/templates/index.html
    }
}