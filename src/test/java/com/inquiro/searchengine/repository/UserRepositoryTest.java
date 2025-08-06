package com.inquiro.searchengine.repository;

import com.inquiro.searchengine.model.User;
import com.inquiro.searchengine.model.User.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindById() {
        User user = new User("aj", "securepass", Role.ADMIN);
        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();

        Optional<User> fetched = userRepository.findById(saved.getId());
        assertThat(fetched).isPresent();
        assertThat(fetched.get().getUsername()).isEqualTo("aj");
    }

    @Test
    void testFindByUsername() {
        User user = new User("kimbrough", "pw123", Role.GUEST);
        userRepository.save(user);

        // Lookup
        Optional<User> result = userRepository.findByUsername("kimbrough");

        assertThat(result).isPresent();
        assertThat(result.get().getRole()).isEqualTo(Role.GUEST);
        assertThat(result.get().getPassword()).isEqualTo("pw123");
    }

    @Test
    void testFindByUsername_NotFound() {
        Optional<User> result = userRepository.findByUsername("nonexistent");
        assertThat(result).isEmpty();
    }
}
