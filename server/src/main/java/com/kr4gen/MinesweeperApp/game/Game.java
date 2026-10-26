package com.kr4gen.MinesweeperApp.game;

public class Game {
    public enum GameState {NOT_ON, ONGOING, WIN, LOSE}

    private final Board_Generator gen;
    private GameState state;

    public Game(int width, int height, int mine) {
        gen = new Board_Generator(height, width, mine);
        this.state = GameState.NOT_ON;
    }

    public int minesLeft() {
        return gen.remainingMines;
    }

    public int getWidth() {
        return gen.width;
    }

    public int getHeight() {
        return gen.height;
    }

    public void clickedLeft(int x, int y) {
        if (state == GameState.ONGOING) {
            if (gen.board[x][y].isMine()) {
                if (!gen.board[x][y].isFlagged) {
                    state = GameState.LOSE;
                }
            } else {
                reveal(x, y);
                if (allSafeCellsRevealed()) {
                    state = GameState.WIN;
                }
            }
        }

    }

    private boolean allSafeCellsRevealed() {
        for (int i = 0; i < gen.height; i++) {
            for (int j = 0; j < gen.width; j++) {
                if (!gen.board[i][j].isMine() && !gen.board[i][j].isRevealed) {
                    return false;
                }
            }
        }
        return true;
    }

    public void clickedRight(int x, int y) {
        if (state == GameState.ONGOING) {
            if (gen.board[x][y].isRevealed) {
                return;
            }

            if (gen.board[x][y].isFlagged) {
                gen.board[x][y].isFlagged = false;
                gen.remainingMines++;
            } else {
                gen.board[x][y].isFlagged = true;
                gen.remainingMines--;
            }

            if (allSafeCellsRevealed()) {
                this.state = GameState.WIN;
            }
        }
    }

    public void startGame(int x, int y) {
        gen.createBoard(x, y);
        reveal(x, y);
    }

    public int getMinesAround(int x, int y) {
        return gen.board[x][y].minesAround;
    }

    public boolean isRevealed(int x, int y) {
        return gen.board[x][y].isRevealed;
    }

    public void reveal(int x, int y) {
        if (x < 0 || x >= gen.height || y < 0 || y >= gen.width) {
            return;
        }

        if (gen.board[x][y].isRevealed) {
            return;
        }

        gen.board[x][y].isRevealed = true;

        if (getMinesAround(x, y) == 0 && !gen.board[x][y].isMine()) {
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (gen.board[i][j].isFlagged) {
                        gen.board[i][j].isFlagged = false;
                    }
                    reveal(i, j);
                }
            }
        }
    }

    public GameState gameOver() {
        return state;
    }

    public boolean isFlagged(int x, int y){
        return gen.board[x][y].isFlagged;
    }

    public int getMines() {
        return gen.remainingMines;
    }
}
