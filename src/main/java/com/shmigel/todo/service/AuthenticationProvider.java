package com.shmigel.todo.service;

import org.springframework.security.core.Authentication;

public interface AuthenticationProvider<P, C> {
    Authentication authentication();
    C credentials();
    P principal();
}
