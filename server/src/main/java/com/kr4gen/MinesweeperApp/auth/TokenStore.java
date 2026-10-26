package com.kr4gen.MinesweeperApp.auth;

import org.springframework.stereotype.Component;
import com.kr4gen.MinesweeperApp.user.User;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class TokenStore {
    private static final Duration dur = Duration.ofHours(12);

    private record userEntry(User user, Instant expiry) {}

    private final Map<String, userEntry> tokens = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    public String issue(User newUser) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);

        String token =
                Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        tokens.put(token, new userEntry(newUser, Instant.now().plus(dur)));

        return token;
    }

    public Optional<User> find(String token) {
        userEntry e = tokens.get(token);

        if (e == null) {
            return Optional.empty();
        }

        if (e.expiry().isBefore(Instant.now())) {
            tokens.remove(token);
            return Optional.empty();
        }

        return Optional.of(e.user());
    }

    public void revoke(String token) {
        tokens.remove(token);
    }
}
