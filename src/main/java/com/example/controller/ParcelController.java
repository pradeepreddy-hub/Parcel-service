package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ParcelController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Welcome to the Simple Parcel Service!");
        return "index";
    }
}

