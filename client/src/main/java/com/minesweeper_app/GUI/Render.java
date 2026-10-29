package com.minesweeper_app.GUI;

import javafx.application.Application;
import javafx.stage.Stage;

public class Render extends Application{

    @Override 
    public void start(Stage stage) {
        stage.setTitle("Minesweeper");
        stage.setScene(new LoginScreen().createScreen(stage));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
