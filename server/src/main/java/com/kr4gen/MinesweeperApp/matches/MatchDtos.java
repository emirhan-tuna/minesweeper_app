package com.kr4gen.MinesweeperApp.matches;

import com.kr4gen.MinesweeperApp.games.Difficulty;
import com.kr4gen.MinesweeperApp.games.Game;
import com.kr4gen.MinesweeperApp.games.Game.GameState;
import com.kr4gen.MinesweeperApp.user.User;

import java.util.List;

public class MatchDtos {

    public enum JoinStatus {WAITING, MATCHED, NONE}

    public record JoinResponse (
            JoinStatus status,
            String matchID
    ) {}

    public record Stats(
            int wins,
            int losses
    ) {}

    public record MatchView (
            String id,
            Difficulty diff,
            int width,
            int height,
            int minesLeft,
            GameState state,
            int elapsedSec,
            List<String> rows,
            String oppUsername,
            int timeLeftUntilStart
    ) {}
}
