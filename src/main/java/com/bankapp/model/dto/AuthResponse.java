package com.bankapp.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AuthResponse {
    @NotNull
    private final String token;
    @NotNull
    private final String tokenType = "Bearer";
    @NotNull
    private final Long expiresIn;
    @NotNull
    private final String email;
    @NotNull
    private final String role;

    public AuthResponse(String token, Long expiresIn, String email, String role) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.email = email;
        this.role = role;
    }

}