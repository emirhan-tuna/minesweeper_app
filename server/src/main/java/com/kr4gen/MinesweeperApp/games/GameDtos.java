package com.kr4gen.MinesweeperApp.games;

import com.kr4gen.MinesweeperApp.games.Game.GameState;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class GameDtos {
    private GameDtos() {}

    public record gameRequest(@NotNull Difficulty diff) {}

    public record moveRequest(@Min(0) int row, @Min(0) int col) {}

    public record gameView(
            String id,
            Difficulty diff,
            int width,
            int height,
            int minesLeft,
            GameState state,
            int elapsedSec,
            List<String> rows
    ) {}
}
