package com.inquiro.searchengine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/image")
    public String imagePage() {
        return "image";
    }

    @GetMapping("/history")
    public String historyPage() {
        return "history";
    }
}
