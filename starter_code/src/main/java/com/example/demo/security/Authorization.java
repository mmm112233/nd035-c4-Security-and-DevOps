package com.example.demo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Authorization {
    private Authorization() {
    }

    public static boolean isRightAccount(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getPrincipal().toString();
        return username.equals(currentUsername);
    }
}
