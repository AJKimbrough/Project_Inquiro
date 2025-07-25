package com.inquiro.searchengine.dto;

import com.inquiro.searchengine.model.SearchResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SearchResultWithSourceTest {

    @Test
    void testField() {
        // Arrange
        SearchResult result = new SearchResult();
        result.setTitle("Java Basics");
        result.setUrl("https://example.com/java");
        result.setDescription("A simple Java tutorial.");

        String engineName = "ExactMatch";

        // Act
        SearchResultWithSource wrapper = new SearchResultWithSource(result, engineName);

        // Assert
        assertEquals("Java Basics", wrapper.getTitle());
        assertEquals("https://example.com/java", wrapper.getUrl());
        assertEquals("A simple Java tutorial.", wrapper.getDescription());
        assertEquals("ExactMatch", wrapper.getEngine());
    }
}
