package com.minesweeper_app.GUI;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public abstract class Screens {
    String name;
    Stage s;

    public Screens(String name) {
        this.name = name;
    }
    public abstract Parent createRoot(Stage s);

    protected void switchTo(Stage stage, Screens aScreen) {
        aScreen.s = stage;
        Parent nextRoot = aScreen.createRoot(stage);
        stage.getScene().setRoot(nextRoot);
    }

}
