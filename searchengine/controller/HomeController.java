package com.inquiro.searchengine.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        boolean authenticated = request.getUserPrincipal() != null;
        boolean isAdmin = authenticated && request.isUserInRole("ADMIN"); // NOTE: no ROLE_ prefix
        model.addAttribute("authenticated", authenticated);
        model.addAttribute("isAdmin", isAdmin);
        return "index";
    }
}
