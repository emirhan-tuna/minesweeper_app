package com.minesweeper_app.GUI;

import com.minesweeper_app.MinesweeperApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class Render extends Application{

    @Override 
    public void start(Stage stage) {
        stage.setTitle("test");
        stage.setScene(new LoginScreen().createScreen(stage));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
