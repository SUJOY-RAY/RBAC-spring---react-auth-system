package com.auth.rbacauth.contracts;

import com.auth.rbacauth.models.Role;

public record AppUser(
        String username,
        String password,
        Role role
) {}
