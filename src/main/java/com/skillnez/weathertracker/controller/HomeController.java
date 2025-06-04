package com.skillnez.weathertracker.controller;

import com.skillnez.weathertracker.dto.SearchFormDto;
import com.skillnez.weathertracker.service.LocationService;
import com.skillnez.weathertracker.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final WeatherService weatherService;
    private final LocationService locationService;

    @Autowired
    public HomeController(WeatherService weatherService, LocationService locationService) {
        this.weatherService = weatherService;
        this.locationService = locationService;
    }


    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        model.addAttribute("username", username);
        if (!model.containsAttribute("searchFormDto")) {
            model.addAttribute("searchForm", new SearchFormDto());
        }
        model.addAttribute("userWeatherList", weatherService.getUserWeatherCards(username));
        return "index"; // /WEB-INF/templates/index.html
    }

    @PostMapping("/")
    public String deleteWeatherCard(@RequestParam("locationId") Long locationId, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        locationService.deleteLocation(locationId, username);
        return "redirect:/";
    }
}