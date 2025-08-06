package com.inquiro.searchengine.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@Component
@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void addAuthFlags(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAuthenticated =
                auth != null && auth.isAuthenticated()
                && auth.getPrincipal() != null
                && !"anonymousUser".equals(auth.getPrincipal());

        String username = isAuthenticated ? auth.getName() : null;

        boolean isAdmin = false;
        if (isAuthenticated && auth.getAuthorities() != null) {
            for (GrantedAuthority a : auth.getAuthorities()) {
                if ("ROLE_ADMIN".equals(a.getAuthority())) {
                    isAdmin = true;
                    break;
                }
            }
        }

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("currentUser", username);
        model.addAttribute("isAdmin", isAdmin);
    }
}
