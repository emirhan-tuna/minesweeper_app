package com.kr4gen.MinesweeperApp.games;

public class Empty_Cell extends Cell{
    public Empty_Cell(int x, int y) {
        super(x, y);
    }

    @Override 
    public boolean isMine() {
        return false;
    }
}
