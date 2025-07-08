package com.inquiro.searchengine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Spring MVC controller that handles web requests
@Controller
public class HomeController {

    @GetMapping("/") // HTTP GET requests to the root URL "/"
    public String index() {
        return "index"; // maps to src/main/resources/templates/index.html
    }
}
