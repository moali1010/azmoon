package com.ebrahimi.azmoon.security.util;


import com.ebrahimi.azmoon.model.UserAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.NoSuchElementException;
import java.util.Optional;

public class SecurityHelper {

    private SecurityHelper() {
    }

    public static UserAccount getCurrentUser() {
        Optional<Authentication> authenticationOptional = getAuthentication();
        Authentication authentication = authenticationOptional.orElseThrow(() -> new NoSuchElementException("Authentication Not Found"));
        return (UserAccount) authentication.getPrincipal();
    }

    public static Optional<Authentication> getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        } else {
            Object principal = authentication.getPrincipal();
            return "anonymousUser".equals(principal) ? Optional.empty() : Optional.of(authentication);
        }
    }
}
