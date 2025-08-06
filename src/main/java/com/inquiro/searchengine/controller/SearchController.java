package com.inquiro.searchengine.controller;

import com.inquiro.searchengine.dto.SearchResultWithSource;
import com.inquiro.searchengine.service.SearchService;
import com.inquiro.searchengine.service.SearchHistoryService; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller // Spring MVC Controller
public class SearchController {

    private final SearchService searchService; 
    private final SearchHistoryService historyService; //logging and retrieving search history

    // dependency for services
    public SearchController(SearchService searchService, SearchHistoryService historyService) {
        this.searchService = searchService;
        this.historyService = historyService;
    }

    @GetMapping("/search") // GET requests for search
    public String search(@RequestParam("query") String query, // Search query string
                         @RequestParam(value = "mode", defaultValue = "OR") String mode, // Search mode (OR, AND, NOT)
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         Model model) { // Model data to view

        // ssearch results with engine source 
        List<SearchResultWithSource> allResults = searchService.search(query, mode);

        // Sort results alphabetically by title (case-insensitive)
        allResults.sort((r1, r2) -> {
            if (r1.getTitle() == null) return (r2.getTitle() == null) ? 0 : 1;
            if (r2.getTitle() == null) return -1;
            return r1.getTitle().compareToIgnoreCase(r2.getTitle());
        });


        // Log query for the current user
        if (query != null && !query.isBlank()) {
            historyService.logSearch(query.trim(), mode);
        }

        final int pageSize = 5;
        int total = allResults.size();
        int totalPages = (int) Math.ceil(total / (double) pageSize);

        // page range
        if (page < 0) page = 0;
        if (totalPages > 0 && page >= totalPages) page = totalPages - 1;

        int from = page * pageSize;
        int to = Math.min(from + pageSize, total);
        List<SearchResultWithSource> pageResults =
                (from < to) ? allResults.subList(from, to) : List.of();


        // Results and search parameters 
        model.addAttribute("results", pageResults);
        model.addAttribute("query", query);
        model.addAttribute("mode", mode);

        // Pagination 
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page + 1 < totalPages);

        return "results";
    }
}
