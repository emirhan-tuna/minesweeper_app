package com.minesweeper_app.gui.board;

import java.util.List;

public interface BoardView {
    String id();
    String diff();
    int width();
    int height();
    int minesLeft();
    String state();
    int elapsedSec();
    List<String> rows();

    boolean isOver();
}
