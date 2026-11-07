package com.minesweeper_app.GUI;

import atlantafx.base.theme.PrimerDark;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Render extends Application{

    @Override 
    public void start(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        stage.setTitle("Minesweeper");
        LoginScreen screen = new LoginScreen();
        screen.s = stage;
        Constants.setSizes(Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
        stage.setScene(new Scene(screen.createRoot(stage)));
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
