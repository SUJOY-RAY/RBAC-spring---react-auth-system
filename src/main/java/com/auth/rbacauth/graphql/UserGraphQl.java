package com.auth.rbacauth.graphql;

import java.util.Map;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class UserGraphQl {
    @QueryMapping
    public Map<String, Object> me(Authentication auth) {
        return Map.of(
                "username", auth.getName(),
                "role", auth.getAuthorities().iterator().next().getAuthority());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @QueryMapping
    public String adminOnly() {
        return "ADMIN secret data 🚨";
    }
}
