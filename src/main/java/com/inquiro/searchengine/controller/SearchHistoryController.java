package com.inquiro.searchengine.controller;

import com.inquiro.searchengine.model.SearchHistory;
import com.inquiro.searchengine.service.SearchHistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SearchHistoryController {

    private final SearchHistoryService historyService;
    

    public SearchHistoryController(SearchHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/history")
    public String myHistory(Model model) {
        List<SearchHistory> history = historyService.getMyHistory();
        model.addAttribute("history", history);
        return "history";
    }

     // clear History
    @PostMapping("/history/clear")
    public String clearHistory() {
        historyService.clearMyHistory();
        return "redirect:/history";
    }
}
