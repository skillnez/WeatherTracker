package com.skillnez.weathertracker.controller;

import com.skillnez.weathertracker.dto.LocationResponseDto;
import com.skillnez.weathertracker.dto.SearchFormDto;
import com.skillnez.weathertracker.dto.UserAuthDto;
import com.skillnez.weathertracker.dto.WeatherApiResponseDto;
import com.skillnez.weathertracker.entity.Location;
import com.skillnez.weathertracker.entity.User;
import com.skillnez.weathertracker.service.WeatherService;
import com.skillnez.weathertracker.service.registration.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class SearchController {

    private final WeatherService weatherService;
    private final UserService userService;

    @Autowired
    public SearchController(WeatherService weatherService, UserService userService) {
        this.weatherService = weatherService;
        this.userService = userService;
    }

    @GetMapping("/search")
    public String search(@ModelAttribute("searchForm") @Valid SearchFormDto searchFormDto,
                         BindingResult bindingResult,
                         HttpServletRequest request,
                         Model model) {
        String username = (String) request.getAttribute("username");
        model.addAttribute("username", username);
        model.addAttribute("addingLocationError", null);
        if (bindingResult.hasErrors()) {
            return "index";
        }
        model.addAttribute("locationsList", weatherService.getGeoByCityName(searchFormDto.getQuery()));
        return "search-results";
    }

    @PostMapping("/search")
    public String addLocation(@ModelAttribute LocationResponseDto locationResponseDto,
                              @RequestParam("query") String query,
                              HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        userService.addLocation(username, locationResponseDto);
        return "redirect:/search?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleAnyRuntime(RuntimeException e, Model model,
                                   @RequestParam("query") String query) {
        model.addAttribute("addingLocationError", e.getMessage());
        return "redirect:/search?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
    }

    //todo добавь @ExceptionHandler для UserService метода addLocation


}
