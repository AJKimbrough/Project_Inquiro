package com.inquiro.searchengine.service;

import com.inquiro.searchengine.dto.SearchResultWithSource;
import com.inquiro.searchengine.model.SearchResult;
import com.inquiro.searchengine.repository.SearchResultRepository;
import com.inquiro.searchengine.util.URLValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service // Spring service component
public class SearchService {

    @Autowired // Injects the repository to access the database
    private SearchResultRepository repository;

    public List<SearchResultWithSource> search(String query, String mode) {
        // Remove special characters, symbols and convert to lowercase
        String cleanQuery = query.trim().replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
        List<String> keywords = getAllStoredKeywords(); // List of known keywords

        // Autocorrect each word in query
        List<String> correctedWords = new ArrayList<>();
        for (String word : cleanQuery.split("\\s+")) {
            correctedWords.add(findClosestKeyword(word, keywords));
        }

        // Join corrected words into string
        String correctedQuery = String.join(" ", correctedWords);
        boolean wasCorrected = !correctedQuery.equalsIgnoreCase(cleanQuery);
        if (wasCorrected) {
            System.out.println("Autocorrected '" + cleanQuery + "' to '" + correctedQuery + "'");
        }

        try {
            ExecutorService executor = Executors.newFixedThreadPool(2); // Thread pool for concurrent engine execution

            // Engine 1: Exact keyword match
            Callable<List<SearchResultWithSource>> exactMatchEngine = () -> {
                List<SearchResult> results = switch (mode.toUpperCase()) {
                    case "AND" -> searchEachWordSeparately(Arrays.asList(cleanQuery.split("\\s+")));
                    case "NOT" -> repository.searchWithNot(cleanQuery);
                    default -> searchEachWordSeparately(Arrays.asList(cleanQuery.split("\\s+")));
                };
                return wrapResultsWithEngine(results, "ExactMatch");
            };

            // Engine 2: Autocorrected search, only runs if correction occurred
            Callable<List<SearchResultWithSource>> autocorrectEngine = () -> {
                if (!wasCorrected) return Collections.emptyList();
                List<SearchResult> results = repository.searchWithOr(correctedQuery);
                return wrapResultsWithEngine(results, "AutoCorrectEngine");
            };

            // Submit tasks to thread pool and retrieve futures
            Future<List<SearchResultWithSource>> exactFuture = executor.submit(exactMatchEngine);
            Future<List<SearchResultWithSource>> autoFuture = executor.submit(autocorrectEngine);

            // Combine results 
            List<SearchResultWithSource> combined = new ArrayList<>();
            combined.addAll(exactFuture.get());
            combined.addAll(autoFuture.get());

            executor.shutdown();

            // Remove duplicate URLs
            Map<String, SearchResultWithSource> deduped = new LinkedHashMap<>();
            for (SearchResultWithSource r : combined) {
                deduped.putIfAbsent(r.getUrl(), r);
            }

            return new ArrayList<>(deduped.values());

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // OR Logic Query 
    private List<SearchResult> searchEachWordSeparately(List<String> words) {
        Set<SearchResult> combinedResults = new LinkedHashSet<>();
        for (String word : words) {
            combinedResults.addAll(repository.searchWithOr(word));
        }
        return new ArrayList<>(combinedResults);
    }

    // Hardcoded keywords, autocorrect 
    private List<String> getAllStoredKeywords() {
        return Arrays.asList("java", "html", "utd", "ai", "dallas", "music", "engineering", "jobs", "openai", "google", "spotify");
    }

    // Levenshtein distance, closest matching keyword using 
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

    // Levenshtein distance between two strings
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

    // Wraps results with engine label for UI display
    private List<SearchResultWithSource> wrapResultsWithEngine(List<SearchResult> results, String engine) {
        List<SearchResultWithSource> wrapped = new ArrayList<>();
        for (SearchResult r : results) {
            wrapped.add(new SearchResultWithSource(r, engine));  // ✅ fixed constructor usage
        }
        return wrapped;
    }

    //URL Validation, remove unreachable URLs
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
