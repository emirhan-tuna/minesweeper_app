package com.minesweeper_app.GUI;

import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class Screens {
    String name;
    Stage s;

    public Screens(String name) {
        this.name = name;
    }
    public abstract Scene createScreen(Stage s);
}
