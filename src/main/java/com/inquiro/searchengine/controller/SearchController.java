package com.inquiro.searchengine.controller;

import com.inquiro.searchengine.dto.SearchResultWithSource;
import com.inquiro.searchengine.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller // Spring MVC Controller
public class SearchController {

    @Autowired // Inject the SearchService instance to handle business logic
    private SearchService searchService;

    @GetMapping("/search") // HTTP GET requests
    public String search(@RequestParam("query") String query,
                         @RequestParam(value = "mode", defaultValue = "OR") String mode,
                         Model model) {
        List<SearchResultWithSource> results = searchService.search(query, mode); // Call the service layer to get search results with engine source labels

        // Add results for view
        model.addAttribute("results", results);
        model.addAttribute("query", query);
        model.addAttribute("mode", mode);
        return "results";
    }
}
