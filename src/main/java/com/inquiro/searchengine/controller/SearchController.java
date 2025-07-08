package com.inquiro.searchengine.controller;

import com.inquiro.searchengine.model.SearchResult;
import com.inquiro.searchengine.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// Spring MVC controller search functionality
@Controller
public class SearchController {

    // Injects the SearchService dependency to handle business logic
    @Autowired
    private SearchService searchService;

    // GET requests to /search
    @GetMapping("/search")
    public String search(@RequestParam("query") String query,
                         @RequestParam(value = "mode", defaultValue = "OR") String mode,
                         Model model) {
        List<SearchResult> results = searchService.search(query, mode); // Search operation to the service layer

        // Data to the model for rendering in the results
        model.addAttribute("results", results);
        model.addAttribute("query", query);
        model.addAttribute("mode", mode);
        return "results";
    }
}