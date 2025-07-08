package com.inquiro.searchengine.service;

import com.inquiro.searchengine.model.SearchResult;
import com.inquiro.searchengine.repository.SearchResultRepository;
import com.inquiro.searchengine.util.URLValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchService {

    @Autowired
    private SearchResultRepository repository;

    public List<SearchResult> search(String query, String mode) {
        String cleanQuery = query.trim().replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
        List<String> keywords = getAllStoredKeywords();

        // Typo correction for each word in the query
        List<String> correctedWords = new ArrayList<>();
        for (String word : cleanQuery.split("\\s+")) {
            correctedWords.add(findClosestKeyword(word, keywords));
        }

        String correctedQuery = String.join(" ", correctedWords);
        if (!correctedQuery.equalsIgnoreCase(cleanQuery)) {
            System.out.println("Autocorrected '" + cleanQuery + "' to '" + correctedQuery + "'");
        }

        return switch (mode.toUpperCase()) {
            case "AND" -> searchAllWordsIndividually(correctedWords);
            case "NOT" -> repository.searchWithNot(correctedQuery);
            default -> repository.searchWithOr(correctedQuery); // OR is default
        };
    }

    private List<SearchResult> searchAllWordsIndividually(List<String> words) {
        Set<SearchResult> combinedResults = new LinkedHashSet<>();
        for (String word : words) {
            combinedResults.addAll(repository.searchWithOr(word));
        }
        return new ArrayList<>(combinedResults);
    }

    // Simulated keyword pool for typo correction
    private List<String> getAllStoredKeywords() {
        return Arrays.asList(
            "java", "html", "utd", "ai", "dallas", "music", "engineering", "jobs", "openai", "google", "spotify"
        );
    }

    private String findClosestKeyword(String query, List<String> keywords) {
        int minDistance = Integer.MAX_VALUE;
        String bestMatch = query;

        for (String keyword : keywords) {
            int distance = computeLevenshtein(query, keyword.toLowerCase());
            if (distance < minDistance) {
                minDistance = distance;
                bestMatch = keyword;
            }
        }

        return (minDistance <= 2) ? bestMatch : query;
    }

    private int computeLevenshtein(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                        dp[i - 1][j - 1],
                        Math.min(dp[i - 1][j], dp[i][j - 1])
                    );
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    // Outdated URL Deletion
    public void deleteOutdatedUrls() {
        List<SearchResult> allResults = repository.findAll();
        for (SearchResult result : allResults) {
            if (!URLValidation.isURLReachable(result.getUrl())) {
                repository.delete(result);
                System.out.println("Deleted unreachable URL: " + result.getUrl());
            }
        }
    }
}
