package com.inquiro.searchengine.controller;

import com.inquiro.searchengine.model.SearchResult;
import com.inquiro.searchengine.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        List<SearchResult> results = searchService.search(query);
        model.addAttribute("results", results);
        model.addAttribute("query", query);
        return "results";
    }
}
