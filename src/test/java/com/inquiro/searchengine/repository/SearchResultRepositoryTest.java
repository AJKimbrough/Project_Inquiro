package com.inquiro.searchengine.repository;

import com.inquiro.searchengine.model.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@EntityScan(basePackages = "com.inquiro.searchengine.model") 
@ComponentScan(basePackages = "com.inquiro.searchengine.repository") 
class SearchResultRepositoryTest {

    @Autowired
    private SearchResultRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        repository.save(new SearchResult("java", "Java Basics", "https://java.com", "Learn Java online."));
        repository.save(new SearchResult("spring", "Spring Framework", "https://spring.io", "Spring Boot tutorial"));
        repository.save(new SearchResult("python", "Python Docs", "https://python.org", "Python basics and syntax."));
    }

    @Test
    void testOrSearch() {
        List<SearchResult> results = repository.orSearch("java");
        assertFalse(results.isEmpty());
        assertEquals("Java Basics", results.get(0).getTitle());
    }

    @Test
    void testAndSearch() {
        List<SearchResult> results = repository.andSearch("java");
        assertEquals(1, results.size());
        assertEquals("Java Basics", results.get(0).getTitle());
    }

    @Test
    void testNotSearch() {
        List<SearchResult> results = repository.notSearch("java");
        assertEquals(2, results.size());
        assertTrue(results.stream().noneMatch(r -> r.getKeyword().contains("java")));
    }

    @Test
    void testFindKeywords() {
        List<String> keywords = repository.findKeywords();
        assertTrue(keywords.contains("java"));
        assertTrue(keywords.contains("spring"));
        assertTrue(keywords.contains("python"));
    }
}
