package com.skillnez.weathertracker.controller;

import com.skillnez.weathertracker.dto.SearchFormDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        model.addAttribute("username", username);
        if (!model.containsAttribute("searchFormDto")) {
            model.addAttribute("searchForm", new SearchFormDto());
        }
        return "index"; // /WEB-INF/templates/index.html
    }
}