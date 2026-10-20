package com.minesweeper_app.BoardGeneration;

public abstract class Cell implements Comparable<Cell>{
    protected boolean isProtected;
    protected int x,y;
    protected int minesAround;
    protected boolean isRevealed;
    protected boolean isFlagged;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.isRevealed = false;
        this.isFlagged = false;
    }

    public void setProtected() {
        this.isProtected = true;
    }

    public void setMinesAround(int numberOfMines) {
        this.minesAround = numberOfMines;
    }

    public abstract boolean isMine();

    @Override 
    public int compareTo(Cell aCell) {
        if (this.x < aCell.x) {
            return -1;
        } else if (this.x > aCell.x) {
            return 1;
        } else {
            if (this.y < aCell.y) {
                return -1;
            } else if (this.y > aCell.y) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
