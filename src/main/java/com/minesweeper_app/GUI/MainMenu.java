package com.minesweeper_app.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu extends Screens{
    public MainMenu(String aName) {
        super(aName);
    }

    @Override 
    public Scene createScreen(Stage stage) {
        BorderPane layout = new BorderPane();
        VBox butBox = new VBox(20);
        Label title = new Label("MINESWEEPER");
        title.setStyle("-fx-font-size: 36; -fx-font-weight: bold;");
        BorderPane.setAlignment(title, Pos.TOP_CENTER);
        BorderPane.setMargin(title, new Insets(40,0,0,0));

        Button startGame = new Button("Start game");
        startGame.setPrefSize(Constants.buttonWidth, Constants.buttonHeight);
        Button times = new Button("See all the recorded times");
        times.setPrefSize(Constants.buttonWidth, Constants.buttonHeight);

        startGame.setOnAction(event -> {
            stage.setScene(new TakeParameters(name).createScreen(stage));
        });
        times.setOnAction(event -> {
            stage.setScene(new AllTimesScene(name).createScreen(stage));
        });

        butBox.getChildren().addAll(startGame, times);
        layout.setCenter(butBox);

        return new Scene(layout, Constants.width, Constants.height);
    } 
}

