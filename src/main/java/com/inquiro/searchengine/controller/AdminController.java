package com.inquiro.searchengine.controller;

import com.inquiro.searchengine.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Spring MVC controller for Admin
@Controller
public class AdminController {

    @Autowired 
    private SearchService searchService;

    @GetMapping("/admin/cleanup") 
    public String cleanupOutdated(Model model) {
        searchService.deleteOutdatedUrls(); // delete unreachable or outdated URLs
        model.addAttribute("message", "Out-of-date URLs removed successfully.");
        return "admin"; 
    }
}
