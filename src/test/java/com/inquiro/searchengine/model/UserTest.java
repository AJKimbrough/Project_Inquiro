package com.inquiro.searchengine.model;

import com.inquiro.searchengine.model.User.Role;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void testConstructorAndGetters() {
        User user = new User("aj", "secure123", Role.ADMIN);

        assertThat(user.getUsername()).isEqualTo("aj");
        assertThat(user.getPassword()).isEqualTo("secure123");
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void testSetters() {
        User user = new User();
        user.setId(1L);
        user.setUsername("kim");
        user.setPassword("strongpass");
        user.setRole(Role.GUEST);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("kim");
        assertThat(user.getPassword()).isEqualTo("strongpass");
        assertThat(user.getRole()).isEqualTo(Role.GUEST);
    }

    @Test
    void testToString() {
        User user = new User("admin", "adminpass", Role.ADMIN);
        String result = user.toString();

        assertThat(result).contains("admin");
        assertThat(result).contains("role=ADMIN"); // Adjusted to match actual toString()
    }
} // ✅ This closing brace was missing
