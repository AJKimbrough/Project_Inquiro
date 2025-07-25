package com.inquiro.searchengine.controller;

import com.inquiro.searchengine.dto.SearchResultWithSource;
import com.inquiro.searchengine.service.SearchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private SearchService searchService;

    @Test
    public void testSearchReturnsResults() throws Exception {
        SearchResultWithSource dummyResult = Mockito.mock(SearchResultWithSource.class);
        when(dummyResult.getTitle()).thenReturn("Java Basics");
        when(dummyResult.getUrl()).thenReturn("https://example.com/java");
        when(dummyResult.getDescription()).thenReturn("Intro to Java");
        when(dummyResult.getEngine()).thenReturn("TestEngine");

        when(searchService.search("java", "OR"))
                .thenReturn(Collections.singletonList(dummyResult));

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
