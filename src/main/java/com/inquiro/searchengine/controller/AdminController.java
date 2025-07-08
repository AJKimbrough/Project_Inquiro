package com.inquiro.searchengine.controller;

import com.inquiro.searchengine.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Spring MVC controller for Admin
@Controller
public class AdminController {

    @Autowired //Injects SearchService dependency to handle business logic
    private SearchService searchService;

    @GetMapping("/admin/cleanup") // GET requests to /admin/cleanup
    public String cleanupOutdated(Model model) {
        searchService.deleteOutdatedUrls(); // Service method to delete unreachable or outdated URLs
        model.addAttribute("message", "Out-of-date URLs removed successfully.");
        return "admin"; // Thymeleaf template
    }
}
