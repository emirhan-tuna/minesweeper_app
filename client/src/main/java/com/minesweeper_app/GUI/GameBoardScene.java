package com.minesweeper_app.GUI;

import com.minesweeper_app.api.ApiClient;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GameBoardScene extends AbstractBoardScene<ApiClient.GameView> {

    public GameBoardScene(ApiClient.GameView game, String aName) {
        super(game, aName);
    }

    @Override
    protected void play(boolean left, int row, int col) {
        try {
            if (left) {
                view = ApiClient.get().reveal(view.id(), row, col);
            } else {
                view = ApiClient.get().flag(view.id(), row, col);
            }
        } catch (ApiClient.ApiError e) {
            error.setText(e.getMessage());
            return;
        }

        refresh();
        if (view.isOver()) {
            end();
        }
    }

    @Override
    public void createEnd(int result) {
        switchTo(s, new EndGameScene(name, result, view));
    }
}
