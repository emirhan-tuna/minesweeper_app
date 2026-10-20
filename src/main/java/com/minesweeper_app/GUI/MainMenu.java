package com.minesweeper_app.GUI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu extends Screens{

    public MainMenu(String aName) {
        super(aName);
    }

    @Override 
    public Scene createScreen(Stage stage) {
        VBox layout = new VBox(20);
        HBox infBox = new HBox();
        Label nameLabel = new Label(name);
        Label title = new Label("MINESWEEPER");
        title.setStyle("-fx-font-size: 36; -fx-font-weight: bold;");

        Button back = new Button("Start game");
        back.setPrefSize(Constants.buttonWidth, Constants.buttonHeight);
        Button times = new Button("See all the recorded times");
        times.setPrefSize(Constants.buttonWidth, Constants.buttonHeight);

        back.setOnAction(event -> {
            stage.setScene(new TakeParameters(name).createScreen(stage));
        });
        times.setOnAction(event -> {
            stage.setScene(new AllTimesScene(name).createScreen(stage));
        });

        infBox.getChildren().addAll(nameLabel);
        layout.getChildren().addAll(title, back, infBox, times);

        return new Scene(layout, Constants.width, Constants.height);
    } 
}

