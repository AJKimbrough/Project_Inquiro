package com.inquiro.searchengine.service;

import com.inquiro.searchengine.dto.SearchResultWithSource;
import com.inquiro.searchengine.model.SearchResult;
import com.inquiro.searchengine.repository.SearchResultRepository;
import com.inquiro.searchengine.service.SearchService;
import com.inquiro.searchengine.util.URLValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private SearchResultRepository repository;

    @InjectMocks
    private SearchService searchService;

    @Test
    void testOrSearch() {
        SearchResult mockResult = new SearchResult("java", "Java Platform", "https://www.oracle.com/java", "Java is a class-based programming language.");
        when(repository.orSearch("java")).thenReturn(List.of(mockResult));

        List<SearchResultWithSource> results = searchService.search("java", "OR");

        assertEquals(1, results.size());
        assertEquals("https://www.oracle.com/java", results.get(0).getUrl());
        assertEquals("ExactMatch", results.get(0).getEngine());
    }

    @Test
    void testAndResults() {
        SearchResult result1 = new SearchResult("java", "Java", "https://a.com", "desc");
        SearchResult result2 = new SearchResult("spring", "Spring", "https://a.com", "desc"); // same URL to trigger intersection

        when(repository.orSearch("java")).thenReturn(List.of(result1));
        when(repository.orSearch("spring")).thenReturn(List.of(result2));

        List<SearchResultWithSource> results = searchService.search("java spring", "AND");

        assertEquals(1, results.size());
        assertEquals("https://a.com", results.get(0).getUrl());
    }

    @Test
    void testNotSearch() {
        SearchResult result1 = new SearchResult("java", "Java Basics", "https://java.com", "Learn Java");
        SearchResult result2 = new SearchResult("java", "Java + Python", "https://python.com", "Combo");

        when(repository.orSearch("java")).thenReturn(List.of(result1, result2));

        List<SearchResultWithSource> results = searchService.search("java python", "NOT");

        assertEquals(1, results.size());
        assertEquals("https://java.com", results.get(0).getUrl());
    }

    @Test
    void testAutoCorrectedSearch() {
        SearchResult correctedResult = new SearchResult("google", "Google", "https://www.google.com", "Search engine");

        when(repository.orSearch("googel")).thenReturn(Collections.emptyList());
        when(repository.orSearch("google")).thenReturn(List.of(correctedResult));

        List<SearchResultWithSource> results = searchService.search("googel", "OR");

        assertEquals(1, results.size());
        assertEquals("AutoCorrectEngine", results.get(0).getEngine());
        assertEquals("https://www.google.com", results.get(0).getUrl());
    }

    @Test
    void testEmptySearch() {
        List<SearchResultWithSource> results = searchService.search(" ", "OR");
        assertTrue(results.isEmpty());
    }

    @Test
    void testDeleteOutdatedUrlsRemoveUnreachableOnes() {
        SearchResult good = new SearchResult("java", "Java", "https://reachable.com", "ok");
        SearchResult bad = new SearchResult("java", "Java", "https://unreachable.com", "fail");

        when(repository.findAll()).thenReturn(List.of(good, bad));

        try (MockedStatic<URLValidation> mockValidation = mockStatic(URLValidation.class)) {
            mockValidation.when(() -> URLValidation.isURLReachable("https://reachable.com")).thenReturn(true);
            mockValidation.when(() -> URLValidation.isURLReachable("https://unreachable.com")).thenReturn(false);

            searchService.deleteOutdatedUrls();

            verify(repository, times(1)).delete(bad);
            verify(repository, never()).delete(good);
        }
    }

    @Test
    void testDuplicateURLs() {
        SearchResult r1 = new SearchResult("java", "Java", "https://dupe.com", "desc");
        SearchResult r2 = new SearchResult("java", "Java Again", "https://dupe.com", "desc2");

        when(repository.orSearch("java")).thenReturn(List.of(r1, r2));

        List<SearchResultWithSource> results = searchService.search("java", "OR");

        assertEquals(1, results.size());
        assertEquals("https://dupe.com", results.get(0).getUrl());
    }
}
