package com.kr4gen.MinesweeperApp.games;

import org.springframework.data.relational.core.sql.In;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class GameSession {
    private final String id = UUID.randomUUID().toString();
    private final String user_id;
    private final Difficulty diff;
    private final Game game;
    private Instant start;
    private Instant end;

    public GameSession(String user_id, Difficulty diff) {
        this.user_id = user_id;
        this.diff = diff;
        this.game = new Game(diff.width, diff.height, diff.mines);
    }

    public void setStart(Instant now) {
        if (start == null) {
            start = now;
        }
    }

    public void setEnd(Instant now) {
        end = now;
    }

    public int gameDuration(Instant now) {
        if (start == null) {
            return 0;
        }

        if (end == null) {
            return (int) Duration.between(start, Instant.now()).toSeconds();
        }
        return (int) Duration.between(start, end).toSeconds();
    }

    public String id() {return id;}
    public String userID() {return user_id;}
    public Difficulty diff() {return diff;}
    public Game game() {return game;}
    public Instant end() {return end;}

}
