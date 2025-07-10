package com.inquiro.searchengine.dto;

import com.inquiro.searchengine.model.SearchResult;

// DTO, wraps a search result with name of engine that returned it
public class SearchResultWithSource {
    private final SearchResult result; // Search result object
    private final String engine; // Resulting engine

    public SearchResultWithSource(SearchResult result, String engine) { // Initialize fields
        this.result = result;
        this.engine = engine;
    }

    // Getters title, URL, description, engine
    public String getTitle() {
        return result.getTitle();
    }

    public String getUrl() {
        return result.getUrl();
    }

    public String getDescription() {
        return result.getDescription();
    }

    public String getEngine() {
        return engine;
    }
}
