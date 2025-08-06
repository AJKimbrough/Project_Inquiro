package com.inquiro.searchengine.service;

import com.inquiro.searchengine.model.SearchHistory;
import com.inquiro.searchengine.model.User;
import com.inquiro.searchengine.model.User.Role;
import com.inquiro.searchengine.repository.SearchHistoryRepository;
import com.inquiro.searchengine.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SearchHistoryServiceTest {

    @Mock
    private SearchHistoryRepository historyRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private SearchHistoryService searchHistoryService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("aj", "securepass", Role.GUEST);

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_GUEST"));
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testLogSearch_savesSearchHistory() {
        when(userRepo.findByUsername("aj")).thenReturn(Optional.of(user));

        searchHistoryService.logSearch("java", "AND");

        verify(historyRepo, times(1)).save(argThat(history ->
                history.getUser().equals(user)
                        && history.getKeyword().equals("java")
                        && history.getMode().equals("AND")
        ));
    }

    @Test
    void testLogSearch_usesDefaultModeIfBlank() {
        when(userRepo.findByUsername("aj")).thenReturn(Optional.of(user));

        searchHistoryService.logSearch("spring", "  ");

        verify(historyRepo, times(1)).save(argThat(history ->
                history.getKeyword().equals("spring")
                        && history.getMode().equals("OR")
        ));
    }

    @Test
    void testGetMyHistory_returnsHistoryList() {
        List<SearchHistory> mockHistory = List.of(
                new SearchHistory(user, "spring", "OR"),
                new SearchHistory(user, "java", "AND")
        );

        when(userRepo.findByUsername("aj")).thenReturn(Optional.of(user));
        when(historyRepo.findByUserOrderBySearchTimeDesc(user)).thenReturn(mockHistory);

        List<SearchHistory> result = searchHistoryService.getMyHistory();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getKeyword()).isEqualTo("spring");
        assertThat(result.get(1).getKeyword()).isEqualTo("java");
    }

    @Test
    void testClearMyHistory_deletesAllForUser() {
        when(userRepo.findByUsername("aj")).thenReturn(Optional.of(user));

        searchHistoryService.clearMyHistory();

        verify(historyRepo, times(1)).deleteByUser(user);
    }
}
