package com.skander.employee_manager.Config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.skander.employee_manager.user.User;

public class ApplicationAuditAware implements AuditorAware<Long> {

    @SuppressWarnings("null")
    @Override 
public Optional<Long> getCurrentAuditor() { // Change from Optional<Integer> to Optional<Long>
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
        return Optional.empty();
    }
    User userPrincipal = (User) authentication.getPrincipal();
    return Optional.ofNullable(userPrincipal.getId()); // This will now return Optional<Long>
}


    
}
