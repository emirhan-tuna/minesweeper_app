package com.minesweeper_app.GUI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

class EndGameScene extends Screens{
    private int result;

    public EndGameScene(String name, int result) {
        super(name);
        this.result = result;
    }

    @Override 
    public Scene createScreen(Stage s) {
        BorderPane pane = new BorderPane();
        Label resultLabel = new Label();
        Button mainMenuButton = new Button("Go back to main menu");

        mainMenuButton.setOnAction(event -> {
            s.setScene(new MainMenu(name).createScreen(s));
        });
        if (result == 0) {
            resultLabel.setText("You won");
        } else {
            resultLabel.setText("You lose");
        }

        pane.setCenter(resultLabel);
        pane.setBottom(mainMenuButton);

        return new Scene(pane, Constants.width, Constants.height);
    }
    
}