package com.auth.rbacauth.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.rbacauth.models.CustomUserDetails;
import com.auth.rbacauth.models.Role;
import com.auth.rbacauth.models.UserEntity;
import com.auth.rbacauth.repositories.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }
}
