package com.kr4gen.MinesweeperApp.games;
import java.util.ArrayList;
import java.util.Random;

public class Board_Generator{
    Cell[][] board;
    ArrayList<Mine_Cell> mineList;
    int height;
    int width;
    int mineNumbers;
    int remainingMines;
    boolean isRevealed;
    Random r = new Random();

    public Board_Generator(int height, int width, int mine) {
        this.height = height;
        this.width = width;
        board = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = new Empty_Cell(i, j);
            }
        }
        mineNumbers = mine;
        remainingMines = mine;
        isRevealed = false;
        mineList = new ArrayList<>();
    }

    public void createBoard(int startX, int startY) {
        setProtectedCells(startX, startY);

        for (int i = 0; i < mineNumbers;) {
            int x = r.nextInt(0 , height);
            int y = r.nextInt(0, width);
            
            if (!board[x][y].isProtected && !board[x][y].isMine()) {
                Mine_Cell newMine = new Mine_Cell(x, y);

                board[x][y] = newMine;
                mineList.add(newMine);
                i++;
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                findMinesAround(i, j);
            }
        }
    }

    public void setProtectedCells(int x, int y) {
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if ((i >= 0 && i < height) && (j >= 0 && j < width)) {
                    board[i][j].setProtected();
                }
            }
        }
    }

    public void findMinesAround(int x, int y) {
        int mines = 0;

        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if ((i >= 0 && i < height) && (j >= 0 && j < width)) {
                    if (board[i][j].isMine()) {
                        mines++;
                    }
                }
            }
        }
        if (board[x][y].isMine()) {
            mines = -1;
        }

        board[x][y].setMinesAround(mines);
    }
}