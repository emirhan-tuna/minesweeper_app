package com.kr4gen.MinesweeperApp.games;

public class Game {
    public enum GameState {NOT_ON, ONGOING, WIN, LOSE}

    private final Board_Generator gen;
    private GameState state;

    public Game(int width, int height, int mine) {
        gen = new Board_Generator(height, width, mine);
        this.state = GameState.NOT_ON;
    }

    public int getWidth() {
        return gen.width;
    }

    public int getHeight() {
        return gen.height;
    }

    public void clickedLeft(int x, int y) {
        if (state == GameState.ONGOING) {
            if (!gen.board[x][y].isFlagged) {
                if (gen.board[x][y].isMine()) {
                    state = GameState.LOSE;
                } else {
                    reveal(x, y);
                    if (allSafeCellsRevealed()) {
                        state = GameState.WIN;
                    }
                }
            }
        } else {
            startGame(x, y);
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
        }
    }

    public void startGame(int x, int y) {
        if (state == GameState.NOT_ON) {
            state = GameState.ONGOING;
            gen.createBoard(x, y);
            reveal(x, y);
        }
    }

    public int getMinesAround(int x, int y) {
        return gen.board[x][y].minesAround;
    }

    public boolean isRevealed(int x, int y) {
        return gen.board[x][y].isRevealed;
    }

    private void reveal(int x, int y) {
        if (x < 0 || x >= gen.height || y < 0 || y >= gen.width) {
            return;
        }

        if (gen.board[x][y].isFlagged) {
            return;
        }

        if (gen.board[x][y].isRevealed) {
            return;
        }

        gen.board[x][y].isRevealed = true;

        if (getMinesAround(x, y) == 0 && !gen.board[x][y].isMine()) {
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    reveal(i, j);
                }
            }
        }
    }

    public GameState getStatus() {
        return state;
    }

    public boolean isFlagged(int x, int y){
        return gen.board[x][y].isFlagged;
    }

    public int getMines() {
        return gen.remainingMines;
    }

    public boolean isOver() {
        return state == GameState.LOSE || state == GameState.WIN;
    }

    public boolean notBounds(int x, int y) {
        return x < 0 || x >= getHeight() || y < 0 || y >= getWidth();
    }

    public boolean isMine(int x, int y) {
        return gen.board[x][y].isMine();
    }
}
