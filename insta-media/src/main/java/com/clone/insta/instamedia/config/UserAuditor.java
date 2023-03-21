package com.clone.insta.instamedia.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAuditor implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return Optional.of(username);
    }
}