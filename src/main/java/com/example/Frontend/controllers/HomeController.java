package com.example.Frontend.controllers;

// =============================================
// HomeController.java - Page 1
// =============================================

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // Team page is fully static in the Thymeleaf template (base.html fragment)
        return "index";
    }
}
