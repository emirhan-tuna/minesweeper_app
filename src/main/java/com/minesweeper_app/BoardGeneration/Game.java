package com.minesweeper_app.BoardGeneration;

import java.util.ArrayList;
import java.util.Collections;

public class Game {
    private Board_Generator gen;
    private boolean active;
    private int winCode;

    public Game(int width, int height, int mine) {
        gen = new Board_Generator(height, width, mine);
        active = true;
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
        if (gen.board[x][y].isMine()) {
            this.winCode = 1;
            active = false;
        } else {
            reveal(x, y);
            if (allSafeCellsRevealed()) {
                this.winCode = 0;
                active = false;
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

        if(allSafeCellsRevealed()) {
            this.winCode = 0;
            this.active = false;
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
                    reveal(i, j);
                }
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    public int gameOver() {
        return winCode;
    }

    public boolean isFlagged(int x, int y){
        return gen.board[x][y].isFlagged;
    }

    public int getMines() {
        return gen.remainingMines;
    }
}
