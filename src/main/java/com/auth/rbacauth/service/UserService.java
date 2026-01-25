package com.auth.rbacauth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.rbacauth.models.Role;
import com.auth.rbacauth.models.UserEntity;
import com.auth.rbacauth.repositories.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @PostConstruct
    void initAdmin() {
        repo.save(
                new UserEntity(
                        "admin",
                        encoder.encode("admin123"),
                        Role.ROLE_ADMIN));
    }

    public void register(String username, String password) {
        if (repo.findByUsername(username).isPresent()) {
            throw new RuntimeException("User already exists.");
        }

        repo.save(
                new UserEntity(
                        username,
                        encoder.encode(password),
                        Role.ROLE_USER));
    }

    public UserEntity load(String username) {
        return repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }
}
