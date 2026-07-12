package com.bankapp.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Setter
    @Getter
    @Id
    @GeneratedValue
    private UUID id;
    @OneToMany
    private List<Account> accounts;
    @Getter
    @Setter
    private String email;
    @Setter
    @Getter
    private String password;
    @Setter
    @Getter
    private String role;

    public User() {
    }

    public User(UUID id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }


}
