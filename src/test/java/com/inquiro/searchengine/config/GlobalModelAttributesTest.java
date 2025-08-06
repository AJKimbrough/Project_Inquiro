package com.inquiro.searchengine.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.util.List;

import static org.mockito.Mockito.*;

class GlobalModelAttributesTest {

    @Mock
    private Model model;

    private GlobalModelAttributes globalModelAttributes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalModelAttributes = new GlobalModelAttributes();
        SecurityContextHolder.clearContext();
    }

    @Test
    void testAddAuthFlags_authenticatedAdmin() {
        var auth = new UsernamePasswordAuthenticationToken(
                "adminUser", "password",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        globalModelAttributes.addAuthFlags(model);

        verify(model).addAttribute("isAuthenticated", true);
        verify(model).addAttribute("currentUser", "adminUser");
        verify(model).addAttribute("isAdmin", true);
    }

    @Test
    void testAddAuthFlags_authenticatedGuest() {
        var auth = new UsernamePasswordAuthenticationToken(
                "guestUser", "password",
                List.of(new SimpleGrantedAuthority("ROLE_GUEST"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        globalModelAttributes.addAuthFlags(model);

        verify(model).addAttribute("isAuthenticated", true);
        verify(model).addAttribute("currentUser", "guestUser");
        verify(model).addAttribute("isAdmin", false);
    }

    @Test
    void testAddAuthFlags_unauthenticatedUser() {
        var auth = new UsernamePasswordAuthenticationToken(
                "anonymousUser", "password"
        );
        auth.setAuthenticated(false);
        SecurityContextHolder.getContext().setAuthentication(auth);

        globalModelAttributes.addAuthFlags(model);

        verify(model).addAttribute("isAuthenticated", false);
        verify(model).addAttribute("currentUser", null);
        verify(model).addAttribute("isAdmin", false);
    }

    @Test
    void testAddAuthFlags_nullAuthentication() {
        SecurityContextHolder.clearContext();

        globalModelAttributes.addAuthFlags(model);

        verify(model).addAttribute("isAuthenticated", false);
        verify(model).addAttribute("currentUser", null);
        verify(model).addAttribute("isAdmin", false);
    }
}
