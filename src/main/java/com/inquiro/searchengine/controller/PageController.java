package com.inquiro.searchengine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Spring MVC controller for handling web page navigation
@Controller
public class PageController {

    // GET requests to "/login" returns login.html
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // GET requests to "/image" returns image.html
    @GetMapping("/image")
    public String imagePage() {
        return "image";
    }

    // GET requests to /history returns history.html
    @GetMapping("/history")
    public String historyPage() {
        return "history";
    }
}
