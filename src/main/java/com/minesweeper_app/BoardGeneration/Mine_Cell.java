package com.minesweeper_app.BoardGeneration;

public class Mine_Cell extends Cell{
    public Mine_Cell(int x, int y) {
        super(x, y);
    }

    @Override 
    public boolean isMine() {
        return true;
    }
}
