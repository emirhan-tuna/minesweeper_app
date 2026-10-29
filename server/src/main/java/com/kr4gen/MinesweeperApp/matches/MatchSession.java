package com.kr4gen.MinesweeperApp.matches;

import com.kr4gen.MinesweeperApp.api.ApiException;
import com.kr4gen.MinesweeperApp.games.Difficulty;
import com.kr4gen.MinesweeperApp.games.Game;
import com.kr4gen.MinesweeperApp.user.User;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MatchSession {
    public enum State {NOT_ON, FINISHED, ONGOING}
    private final static Duration TIMEOUT = Duration.ofSeconds(30);

    private final String id = UUID.randomUUID().toString();
    private final Difficulty diff;
    private final User userOne;
    private final User userTwo;
    private final Map<String, Game> gameMap = new ConcurrentHashMap<>();
    private State gameState = State.NOT_ON;
    private User winner;
    private Instant playerOneLastPlayed;
    private Instant playerTwoLastPlayed;
    private Instant start;
    private Instant end;
    private final Random r = new Random();
    private final long seed;

    public MatchSession(User userOne, User userTwo, Difficulty diff) {
        this.diff = diff;
        this.userOne = userOne;
        this.userTwo = userTwo;
        this.seed = r.nextLong();
        Game tempGameOne = new Game(diff.width, diff.height, diff.mines, seed);
        tempGameOne.startSetGame();
        Game tempGameTwo = new Game(diff.width, diff.height, diff.mines, seed);
        tempGameTwo.startSetGame();

        setLastPlayed(Instant.now(), userOne.id());
        setLastPlayed(Instant.now(), userTwo.id());

        gameMap.put(userOne.id(), tempGameOne);
        gameMap.put(userTwo.id(), tempGameTwo);

        setStart(Instant.now());
    }

    public void setStart(Instant now) {
        if (start == null) {
            this.start = now;
        }
        if (this.gameState == State.NOT_ON) {
            this.gameState = State.ONGOING;
        }
    }

    public void setEnd(Instant now) {
        if (end == null) {
            end = now;
        }
    }

    public int gameDuration(Instant now) {
        if (start == null) {
            return 0;
        }

        if (end == null) {
            return (int) Duration.between(start, now).toSeconds();
        }
        return (int) Duration.between(start, end).toSeconds();
    }

    public User getOpponent(String id) {
        if (isPlayer(id)) {
            return id.equals(userOne.id()) ? userTwo : userOne;
        }
        throw new ApiException(HttpStatus.FORBIDDEN, "It is not a player in the match");
    }

    public boolean isPlayer(String id) {
        return userOne.id().equals(id) || userTwo.id().equals(id);
    }

    public Game getGame(String id) {
        if (isPlayer(id)) {
            return gameMap.get(id);
        }
        throw new ApiException(HttpStatus.FORBIDDEN, "Is not a player in the match");
    }

    public void setLastPlayed(Instant now, String id) {
        if (isPlayer(id)) {
            if (id.equals(userOne.id())) {
                playerOneLastPlayed = now;
            } else {
                playerTwoLastPlayed = now;
            }
        } else {
            throw new ApiException(HttpStatus.FORBIDDEN, "It is not a player in the match");
        }

    }

    public Optional<User> inactive(Instant now) {
        if (Duration.between(playerOneLastPlayed, now).toSeconds() > TIMEOUT.toSeconds()) {
            return Optional.of(userOne);
        } else if (Duration.between(playerTwoLastPlayed, now).toSeconds() > TIMEOUT.toSeconds()) {
            return Optional.of(userTwo);
        }
        return Optional.empty();
    }

    public void finish(Instant now, User winner) {
        if (gameState == State.FINISHED) {
            return;
        }
        gameState = State.FINISHED;
        setEnd(now);
        this.winner = winner;
    }
}
