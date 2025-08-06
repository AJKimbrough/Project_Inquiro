package com.inquiro.searchengine.config;

import com.inquiro.searchengine.model.User;
import com.inquiro.searchengine.model.User.Role;
import com.inquiro.searchengine.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(
            UserRepository repo,
            BCryptPasswordEncoder encoder
    ) {
        return args -> {
            repo.findByUsername("admin").orElseGet(() ->
                repo.save(new User("admin", encoder.encode("password123"), Role.ADMIN))
            );
            repo.findByUsername("guest").orElseGet(() ->
                repo.save(new User("guest", encoder.encode("password123"), Role.GUEST))
            );
        };
    }
}
