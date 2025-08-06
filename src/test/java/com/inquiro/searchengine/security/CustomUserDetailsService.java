package com.inquiro.searchengine.security;

import com.inquiro.searchengine.model.User;
import com.inquiro.searchengine.model.User.Role;
import com.inquiro.searchengine.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    public CustomUserDetailsServiceTest() {
        openMocks(this); 
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        
        User user = new User("aj", "securepass", Role.ADMIN);
        when(userRepository.findByUsername("aj")).thenReturn(Optional.of(user));


        UserDetails userDetails = customUserDetailsService.loadUserByUsername("aj");


        assertThat(userDetails.getUsername()).isEqualTo("aj");
        assertThat(userDetails.getPassword()).isEqualTo("securepass");
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("missing");
        });
    }
}
