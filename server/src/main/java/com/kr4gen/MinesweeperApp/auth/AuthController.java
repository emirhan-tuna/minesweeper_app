package com.kr4gen.MinesweeperApp.auth;

import com.kr4gen.MinesweeperApp.auth.AuthDtos.AuthResponse;
import com.kr4gen.MinesweeperApp.auth.AuthDtos.Credentials;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody Credentials c) {
        return service.register(c);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody Credentials c) {
        return service.login(c);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
        service.logout(header.substring(7));
    }
}
