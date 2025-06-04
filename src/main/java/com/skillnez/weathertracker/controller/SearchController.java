package com.skillnez.weathertracker.controller;

import com.skillnez.weathertracker.dto.LocationResponseDto;
import com.skillnez.weathertracker.dto.SearchFormDto;
import com.skillnez.weathertracker.exception.LocationAddingException;
import com.skillnez.weathertracker.service.LocationService;
import com.skillnez.weathertracker.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class SearchController {

    private final WeatherService weatherService;
    private final LocationService locationService;

    @Autowired
    public SearchController(WeatherService weatherService, LocationService locationService) {
        this.weatherService = weatherService;
        this.locationService = locationService;
    }

    @GetMapping("/search")
    public String search(@ModelAttribute("searchForm") @Valid SearchFormDto searchFormDto,
                         BindingResult bindingResult,
                         HttpServletRequest request,
                         Model model) {
        String username = (String) request.getAttribute("username");
        model.addAttribute("username", username);
        model.getAttribute("addingLocationError");
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
        locationService.addLocation(username, locationResponseDto);
        return "redirect:/search?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
    }

    @ExceptionHandler(LocationAddingException.class)
    public String handleLocationAddingException(LocationAddingException e,
                                         RedirectAttributes redirectAttributes,
                                         HttpServletRequest request) {
        String query = request.getParameter("query");
        if (query == null) query = "";
        redirectAttributes.addFlashAttribute("addingLocationError", e.getMessage());
        return "redirect:/search?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
    }
}
