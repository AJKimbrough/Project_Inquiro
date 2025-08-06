package com.inquiro.searchengine.service;

import com.inquiro.searchengine.model.SearchHistory;
import com.inquiro.searchengine.model.User;
import com.inquiro.searchengine.repository.SearchHistoryRepository;
import com.inquiro.searchengine.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SearchHistoryService {

    private final SearchHistoryRepository historyRepo;
    private final UserRepository userRepo;

    public SearchHistoryService(SearchHistoryRepository historyRepo, UserRepository userRepo) {
        this.historyRepo = historyRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public void logSearch(String keyword, String mode) {
        String username = currentUsername();
        if (username == null || keyword == null || keyword.isBlank()) return;

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found: " + username));

        String normalizedMode = (mode == null || mode.isBlank()) ? "OR" : mode.toUpperCase();

         historyRepo.save(new SearchHistory(user, keyword.trim(), normalizedMode));

    }

    @Transactional
    public void clearMyHistory() {
        String username = currentUsername();
        if (username == null) return;
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found: " + username));
        historyRepo.deleteByUser(user);
    }    

    @Transactional(readOnly = true)
    public List<SearchHistory> getMyHistory() {
        String username = currentUsername();
        if (username == null) return List.of();

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found: " + username));

        return historyRepo.findByUserOrderBySearchTimeDesc(user);
    }

    private String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()
                && auth.getPrincipal() != null
                && !"anonymousUser".equals(auth.getPrincipal()))
                ? auth.getName()
                : null;
    }
}
