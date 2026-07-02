package com.bankapp.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;

}
