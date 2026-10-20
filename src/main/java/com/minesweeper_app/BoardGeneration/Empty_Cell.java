package com.minesweeper_app.BoardGeneration;

public class Empty_Cell extends Cell{
    public Empty_Cell(int x, int y) {
        super(x, y);
    }

    @Override 
    public boolean isMine() {
        return false;
    }
}
