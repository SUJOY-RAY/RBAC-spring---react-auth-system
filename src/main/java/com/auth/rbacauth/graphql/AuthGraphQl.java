package com.auth.rbacauth.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import com.auth.rbacauth.models.UserEntity;
import com.auth.rbacauth.security.CookieUtil;
import com.auth.rbacauth.security.JwtService;
import com.auth.rbacauth.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthGraphQl {
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;

    @MutationMapping
    public Boolean register(@Argument String username, @Argument String password) {
        userService.register(username, password);
        return true;
    }

    @MutationMapping
    public Boolean login(
            @Argument String username,
            @Argument String password,
            ServerHttpResponse response) {
        UserEntity user = userService.load(username);

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid Credentials.");
        }

        String token = jwtService.generate(user);

        response.getHeaders().add(HttpHeaders.SET_COOKIE,
                cookieUtil.jwtCookie(token).toString());

        return true;
    }

    @MutationMapping
    public Boolean logout(ServerHttpResponse response) {
        response.getHeaders().add(
                HttpHeaders.SET_COOKIE,
                cookieUtil.clear().toString());
        return true;
    }
}
