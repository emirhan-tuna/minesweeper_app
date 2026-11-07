package com.minesweeper_app.gui;

import javafx.scene.Parent;
import javafx.stage.Stage;

public abstract class Screens {
    protected String name;
    protected Stage s;

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
