package com.inquiro.searchengine.controller;

import com.inquiro.searchengine.dto.SearchResultWithSource;
import com.inquiro.searchengine.service.SearchService;
import com.inquiro.searchengine.service.SearchHistoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable Spring Security filters
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean 
    private SearchService searchService;

    @SuppressWarnings("removal")
    @MockBean
    private SearchHistoryService historyService;

    @Test
    void testSearchReturnsResults() throws Exception {
        // Create a dummy search result
        SearchResultWithSource dummyResult = Mockito.mock(SearchResultWithSource.class);
        when(dummyResult.getTitle()).thenReturn("Java Basics");
        when(dummyResult.getUrl()).thenReturn("https://example.com/java");
        when(dummyResult.getDescription()).thenReturn("Intro to Java");
        when(dummyResult.getEngine()).thenReturn("TestEngine");

        // Mock SearchService response
        when(searchService.search("java", "OR"))
                .thenReturn(Collections.singletonList(dummyResult));

        // Perform GET request and validate
        mockMvc.perform(get("/search")
                        .param("query", "java")
                        .param("mode", "OR"))
                .andExpect(status().isOk())
                .andExpect(view().name("results"))
                .andExpect(model().attributeExists("results"))
                .andExpect(model().attribute("query", "java"))
                .andExpect(model().attribute("mode", "OR"));
    }
}
