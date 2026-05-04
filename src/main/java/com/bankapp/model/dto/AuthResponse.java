package com.bankapp.model.dto;

public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private String email;
    private String role;

    public AuthResponse(String token, Long expiresIn, String email, String role) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

}