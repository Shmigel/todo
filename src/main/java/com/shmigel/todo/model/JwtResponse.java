package com.shmigel.todo.model;

import lombok.Data;

import java.util.UUID;

@Data
public class JwtResponse {
    private String authToken;
    private long expireIn;
    private UUID userId;

    public JwtResponse() {
    }

    public JwtResponse(String authToken, long expireIn, UUID userId) {
        this.authToken = authToken;
        this.expireIn = expireIn;
        this.userId = userId;
    }
}
