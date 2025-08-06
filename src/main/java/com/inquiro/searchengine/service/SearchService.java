package com.inquiro.searchengine.service;

import com.inquiro.searchengine.dto.SearchResultWithSource;
import com.inquiro.searchengine.model.SearchResult;
import com.inquiro.searchengine.repository.SearchResultRepository;
import com.inquiro.searchengine.util.URLValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service
public class SearchService {

    @Autowired
    private SearchResultRepository repository;

    // Main search method handles query logic and parallel engine execution
    public List<SearchResultWithSource> search(String query, String mode) {
        String cleanQuery = query.trim().replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase(); // clean input
        List<String> keywords = getKeywords(); // keyword list

        // Autocorrect find closest keywords for each word
        List<String> correctedWords = new ArrayList<>();
        for (String word : cleanQuery.split("\\s+")) {
            correctedWords.add(findClosestMatch(word, keywords));
        }

        String correctedQuery = String.join(" ", correctedWords);
        boolean wasCorrected = !correctedQuery.equalsIgnoreCase(cleanQuery);
        if (wasCorrected) {
            System.out.println("Autocorrected '" + cleanQuery + "' to '" + correctedQuery + "'");
        }

        try {
            ExecutorService executor = Executors.newFixedThreadPool(2); // Concurrent execution

            // Engine 1: Exact keyword match using selected mode
            Callable<List<SearchResultWithSource>> exactMatchEngine = () -> {
                List<SearchResult> results;

                switch (mode.toUpperCase()) {
                    case "AND" -> {
                        results = andSearch(Arrays.asList(cleanQuery.split("\\s+")));
                    }
                    case "NOT" -> {
                    List<String> words = Arrays.asList(cleanQuery.split("\\s+"));
                    if (words.isEmpty()) return Collections.emptyList();

                    String includeTerm = words.get(0);  // include this term
                    List<String> excludeTerms = words.subList(1, words.size());  // exclude these

                    List<SearchResult> baseResults = repository.orSearch(includeTerm);
                    results = baseResults.stream()
                        .filter(result -> excludeTerms.stream().noneMatch(exclude ->
                                result.getTitle().toLowerCase().contains(exclude) ||
                                result.getDescription().toLowerCase().contains(exclude) ||
                                result.getKeyword().toLowerCase().contains(exclude)
                        ))
                        .toList();
                    }
                    default -> {
                        results = orSearch(Arrays.asList(cleanQuery.split("\\s+")));
                    }
                }

                return resultsWithEngine(results, "ExactMatch");
            };


            // Engine 2: Autocorrect query only runs if correction occurred
            Callable<List<SearchResultWithSource>> autocorrectEngine = () -> {
                if (!wasCorrected) return Collections.emptyList();
                List<SearchResult> results = repository.orSearch(correctedQuery);
                return resultsWithEngine(results, "AutoCorrectEngine");
            };

            // Run engines concurrently
            Future<List<SearchResultWithSource>> exactFuture = executor.submit(exactMatchEngine);
            Future<List<SearchResultWithSource>> autoFuture = executor.submit(autocorrectEngine);

            List<SearchResultWithSource> combined = new ArrayList<>();
            combined.addAll(exactFuture.get());
            combined.addAll(autoFuture.get());

            executor.shutdown(); // close thread pool

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

    // OR union of all keyword matches
    private List<SearchResult> orSearch(List<String> words) {
        Set<SearchResult> combinedResults = new LinkedHashSet<>();
        for (String word : words) {
            combinedResults.addAll(repository.orSearch(word));
        }
        return new ArrayList<>(combinedResults);
    }

    // AND intersection of keyword matches using OR-query results
    private List<SearchResult> andSearch(List<String> words) {
    List<Set<SearchResult>> resultSets = new ArrayList<>();

    for (String word : words) {
        List<SearchResult> wordResults = repository.orSearch(word); // still using OR match
        resultSets.add(new HashSet<>(wordResults));
    }

    if (resultSets.isEmpty()) return Collections.emptyList();

    // Intersect all result sets
    Set<SearchResult> intersection = new HashSet<>(resultSets.get(0));
    for (int i = 1; i < resultSets.size(); i++) {
        intersection.retainAll(resultSets.get(i));
    }

    return new ArrayList<>(intersection);
}


    // Return list of known search keywords
    private List<String> getKeywords() {
        return Arrays.asList("java", "html", "utd", "ai", "dallas", "music", "engineering", "jobs", "openai", "google", "spotify", "cowboys", "wings", "mavericks", "goldman", "sachs", "admissions", "youtube", "spotify", "amazon", "linkedin", "apple");
    }

    // Find closest keyword using Levenshtein distance
    private String findClosestMatch(String query, List<String> keywords) {
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

    // Compute Levenshtein edit distance
    private int computeLevenshtein(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) dp[i][j] = j;
                else if (j == 0) dp[i][j] = i;
                else if (s1.charAt(i - 1) == s2.charAt(j - 1)) dp[i][j] = dp[i - 1][j - 1];
                else dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                        Math.min(dp[i - 1][j], dp[i][j - 1]));
            }
        }

        return dp[s1.length()][s2.length()];
    }

    // Wrap result with engine name for UI
    private List<SearchResultWithSource> resultsWithEngine(List<SearchResult> results, String engine) {
        List<SearchResultWithSource> wrapped = new ArrayList<>();
        for (SearchResult r : results) {
            wrapped.add(new SearchResultWithSource(r, engine));  // use SearchResult DTO wrapper
        }
        return wrapped;
    }

    // Remove unreachable URLs from database
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
