package com.inquiro.searchengine.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SearchResultTest {

    @Test
    void testSettersAndGetters() {
        SearchResult result = new SearchResult();
        result.setId(1L);
        result.setKeyword("java");
        result.setTitle("Java Platform");
        result.setUrl("https://example.com/java");
        result.setDescription("Intro to Java");

        assertEquals(1L, result.getId());
        assertEquals("java", result.getKeyword());
        assertEquals("Java Platform", result.getTitle());
        assertEquals("https://example.com/java", result.getUrl());
        assertEquals("Intro to Java", result.getDescription());
    }

    @Test
    void testUrlSame() {
        SearchResult r1 = new SearchResult("java", "Java", "https://example.com", "desc");
        SearchResult r2 = new SearchResult("java", "Java", "https://example.com", "desc");

        assertEquals(r1.getUrl(), r2.getUrl());
        assertEquals(r1.getKeyword(), r2.getKeyword());
        assertEquals(r1.getTitle(), r2.getTitle());
        assertEquals(r1.getDescription(), r2.getDescription());
    }

    @Test
    void testUrlDiff() {
        SearchResult r1 = new SearchResult("java", "Java", "https://example.com", "desc");
        SearchResult r2 = new SearchResult("java", "Java", "https://different.com", "desc");

        assertNotEquals(r1.getUrl(), r2.getUrl());
    }

    @Test
    void testNullUrls() {
        SearchResult r1 = new SearchResult("java", "Java", null, "desc");
        SearchResult r2 = new SearchResult("java", "Java", null, "desc");

        assertNull(r1.getUrl());
        assertNull(r2.getUrl());
        assertEquals(r1.getKeyword(), r2.getKeyword());
        assertEquals(r1.getTitle(), r2.getTitle());
        assertEquals(r1.getDescription(), r2.getDescription());
    }
}
