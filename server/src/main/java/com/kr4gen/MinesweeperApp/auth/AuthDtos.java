package com.kr4gen.MinesweeperApp.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
    private AuthDtos() {}

    public record Credentials(
            @NotBlank @Size(max = 50) String username,
            @NotBlank @Size(max = 50) String password) {}

    public record AuthResponse(String token, String username) {}
}
