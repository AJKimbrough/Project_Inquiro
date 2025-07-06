package com.inquiro.searchengine.service;

import com.inquiro.searchengine.model.SearchResult;
import com.inquiro.searchengine.repository.SearchResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private SearchResultRepository repository;

    public List<SearchResult> search(String query) {
        String keywordPart = query.trim().toLowerCase();
        String searchTerm = query.trim().toLowerCase();

        return repository.searchFlexible(keywordPart, searchTerm);
    }
}
