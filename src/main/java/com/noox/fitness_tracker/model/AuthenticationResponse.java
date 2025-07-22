package com.noox.fitness_tracker.model;

public class AuthenticationResponse {

    private final String jwt;
    private final String username;
    private final String email;
    private final String role;

    public AuthenticationResponse(String jwt, String username, String email, String role) {
        this.jwt = jwt;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getJwt() {
        return jwt;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
