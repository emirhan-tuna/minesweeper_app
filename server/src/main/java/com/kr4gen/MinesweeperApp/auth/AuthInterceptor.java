package com.kr4gen.MinesweeperApp.auth;

import com.kr4gen.MinesweeperApp.api.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import org.springframework.http.HttpHeaders;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    public static final String USER = "currentUser";

    private final TokenStore tokens;

    public AuthInterceptor(TokenStore tokens) {
        this.tokens = tokens;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            var user = tokens.find(header.substring(7));
            if (user.isPresent()) {
                request.setAttribute(USER, user.get());
                return true;
            }
        }
        throw new ApiException(HttpStatus.UNAUTHORIZED, "Login required");
    }
}
