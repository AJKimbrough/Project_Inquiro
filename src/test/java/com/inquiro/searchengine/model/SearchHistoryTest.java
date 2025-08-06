package com.inquiro.searchengine.model;

import com.inquiro.searchengine.repository.SearchHistoryRepository;
import com.inquiro.searchengine.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest 
@Transactional   
public class SearchHistoryTest {

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSearchHistory() {
        User user = new User("testuser", "testpass", User.Role.GUEST);
        user = userRepository.save(user);

        SearchHistory entry = new SearchHistory(user, "java", "OR");
        SearchHistory saved = searchHistoryRepository.save(entry);

        Optional<SearchHistory> fetched = searchHistoryRepository.findById(saved.getId());
        assertThat(fetched).isPresent();

        SearchHistory history = fetched.get();
        assertThat(history.getUser().getUsername()).isEqualTo("testuser");
        assertThat(history.getKeyword()).isEqualTo("java");
        assertThat(history.getMode()).isEqualTo("OR");
        assertThat(history.getSearchTime()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}
