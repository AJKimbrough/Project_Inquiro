package com.inquiro.searchengine.repository;

import com.inquiro.searchengine.model.SearchHistory;
import com.inquiro.searchengine.model.User;
import com.inquiro.searchengine.model.User.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SearchHistoryRepositoryTest {

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("historyuser", "password123", Role.GUEST);
        user = userRepository.save(user);

        searchHistoryRepository.save(new SearchHistory(user, "java", "OR"));
        try { Thread.sleep(10); } catch (InterruptedException ignored) {}
        searchHistoryRepository.save(new SearchHistory(user, "spring", "AND"));
        try { Thread.sleep(10); } catch (InterruptedException ignored) {}
        searchHistoryRepository.save(new SearchHistory(user, "boot", "NOT"));
    }

    @Test
    void testFindByUserOrderBySearchTimeDesc() {
        List<SearchHistory> results = searchHistoryRepository.findByUserOrderBySearchTimeDesc(user);

        assertThat(results).hasSize(3);
        assertThat(results.get(0).getKeyword()).isEqualTo("boot");
        assertThat(results.get(1).getKeyword()).isEqualTo("spring");
        assertThat(results.get(2).getKeyword()).isEqualTo("java");
    }

    @Test
    void testDeleteByUser() {
        searchHistoryRepository.deleteByUser(user);
        List<SearchHistory> results = searchHistoryRepository.findByUserOrderBySearchTimeDesc(user);
        assertThat(results).isEmpty();
    }
}
