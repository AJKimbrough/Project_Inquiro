package com.inquiro.searchengine.controller;

import com.inquiro.searchengine.model.SearchHistory;
import com.inquiro.searchengine.service.SearchHistoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchHistoryController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable Spring Security during test
class SearchHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private SearchHistoryService historyService;

    @Test
    void testGetHistory() throws Exception {
        // Prepare dummy history
        SearchHistory mockEntry = Mockito.mock(SearchHistory.class);
        when(historyService.getMyHistory()).thenReturn(List.of(mockEntry));

        mockMvc.perform(get("/history"))
               .andExpect(status().isOk())
               .andExpect(view().name("history"))
               .andExpect(model().attributeExists("history"));

        verify(historyService).getMyHistory();
    }

    @Test
    void testClearHistory() throws Exception {
        mockMvc.perform(post("/history/clear"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/history"));

        verify(historyService).clearMyHistory();
    }
}
