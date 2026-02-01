package com.auth.rbacauth.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import com.auth.rbacauth.security.CookieUtil;
import com.auth.rbacauth.security.JwtService;
import com.auth.rbacauth.service.UserService;

import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthGraphQl {
    private final UserService userService;
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;
    private final AuthenticationManager authManager;

    @MutationMapping
    public Boolean register(@Argument String username, @Argument String password) {
        userService.register(username, password);
        return true;
    }

    @MutationMapping
    public Boolean login(@Argument String username,
            @Argument String password,
            GraphQLContext context) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(auth);

        String token = jwtService.generate(auth);

        HttpServletResponse response = context.get("response");
        if (response == null) {
            throw new RuntimeException("HttpServletResponse not available in GraphQL context!");
        }

        Cookie cookie = new Cookie(cookieUtil.cookieName(), token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(15 * 60);
        response.addCookie(cookie);

        return true;
    }

    @MutationMapping
    public Boolean logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieUtil.cookieName(), "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); 
        response.addCookie(cookie);
        return true;
    }

}
