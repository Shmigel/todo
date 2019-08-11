package com.shmigel.todo.service;

import com.shmigel.todo.model.UserAuthDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthenticationProviderImpl implements AuthenticationProvider<UserAuthDetails, UUID> {
    @Override
    public Authentication authentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UUID credentials() {
        return (UUID) authentication().getCredentials();
    }

    @Override
    public UserAuthDetails principal() {
        return (UserAuthDetails) authentication().getPrincipal();
    }
}
