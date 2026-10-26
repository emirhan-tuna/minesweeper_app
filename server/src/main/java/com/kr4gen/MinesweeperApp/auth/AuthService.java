package com.kr4gen.MinesweeperApp.auth;

import com.kr4gen.MinesweeperApp.api.ApiException;
import com.kr4gen.MinesweeperApp.auth.AuthDtos.AuthResponse;
import com.kr4gen.MinesweeperApp.auth.AuthDtos.Credentials;
import com.kr4gen.MinesweeperApp.user.User;
import com.kr4gen.MinesweeperApp.user.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private UserRepository users;
    private TokenStore tokens;

    public AuthService(UserRepository aUsers, TokenStore aTokens) {
        this.users = aUsers;
        this.tokens = aTokens;
    }

    public AuthResponse register(Credentials c) {
        String username = c.username().trim();

        try {
            User user = users.create(username, BCrypt.hashpw(c.password(), BCrypt.gensalt()));
            return new AuthResponse(tokens.issue(user), user.username());
        } catch (DuplicateKeyException e) {
            throw new ApiException(HttpStatus.CONFLICT, "Username taken");
        }
    }

    public AuthResponse login(Credentials c) {
        String username = c.username().trim();

        User user = users.findByUsername(username)
                .filter(u -> BCrypt.checkpw(c.password(), u.passwordHash()))
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Username or password wrong"));

        return new AuthResponse(tokens.issue(user), user.username());
    }

    public void logout(String token) {
        tokens.revoke(token);
    }
}
