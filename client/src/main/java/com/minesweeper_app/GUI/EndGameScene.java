package com.minesweeper_app.GUI;

import com.minesweeper_app.api.ApiClient;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

class EndGameScene extends AbstractEndScene{

    public EndGameScene(String name, int result, ApiClient.GameView resultPane) {
        super(name, result, resultPane);
    }

    @Override
    public Label setResultLabel() {
        Label label = new Label();

        if (result == 0) {
            label.setText("You cleared the board! Your time is " + view.elapsedSec());
        } else {
            label.setText("You clicked a mine!");
        }

        return label;
    }
}