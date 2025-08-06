package com.inquiro.searchengine.config;

import com.inquiro.searchengine.model.User;
import com.inquiro.searchengine.model.User.Role;
import com.inquiro.searchengine.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.CommandLineRunner;


import java.util.Optional;

import static org.mockito.Mockito.*;

class DataInitializerTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private DataInitializer initializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInitUsers_createsUsersIfNotExist() throws Exception {
        when(userRepo.findByUsername("admin")).thenReturn(Optional.empty());
        when(userRepo.findByUsername("guest")).thenReturn(Optional.empty());
        when(encoder.encode("password123")).thenReturn("encodedPass");

        CommandLineRunner runner = initializer.initUsers(userRepo, encoder);
        runner.run();

        verify(userRepo).save(argThat(user ->
                user.getUsername().equals("admin") &&
                user.getRole() == Role.ADMIN &&
                user.getPassword().equals("encodedPass")
        ));

        verify(userRepo).save(argThat(user ->
                user.getUsername().equals("guest") &&
                user.getRole() == Role.GUEST &&
                user.getPassword().equals("encodedPass")
        ));
    }

    @Test
    void testInitUsers_doesNotCreateIfUsersExist() throws Exception {
        when(userRepo.findByUsername("admin")).thenReturn(Optional.of(new User()));
        when(userRepo.findByUsername("guest")).thenReturn(Optional.of(new User()));

        CommandLineRunner runner = initializer.initUsers(userRepo, encoder);
        runner.run();

        verify(userRepo, never()).save(any());
    }
}
