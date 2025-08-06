package com.inquiro.searchengine.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Test
    void contextLoads() {
        assertThat(passwordEncoder).isNotNull();
        assertThat(securityFilterChain).isNotNull();
    }
}
