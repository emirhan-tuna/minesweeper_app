package com.minesweeper_app.gui.end;

import com.minesweeper_app.api.ApiClient;
import javafx.scene.control.Label;

public class EndGameScene extends AbstractEndScene {

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