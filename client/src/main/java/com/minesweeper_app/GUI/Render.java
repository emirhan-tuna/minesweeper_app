package com.minesweeper_app.GUI;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.stage.Stage;

public class Render extends Application{

    @Override 
    public void start(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        stage.setTitle("Minesweeper");
        stage.setScene(new LoginScreen().createScreen(stage));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
