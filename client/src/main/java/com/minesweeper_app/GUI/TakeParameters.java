package com.minesweeper_app.GUI;

import com.minesweeper_app.api.ApiClient;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TakeParameters extends Screens{
    public final boolean isGame;
    private Label error = new Label();
    public TakeParameters(String aName, boolean game) {
        super(aName);
        this.isGame = game;
    }

    @Override 
    public Parent createRoot(Stage s) {
        VBox pane = new VBox(15);
        pane.setAlignment(Pos.CENTER);

        Button buttonSmall = new Button("Small");
        Button buttonMed = new Button("Middle");
        Button buttonBig = new Button("Big");

        buttonSmall.setOnAction(event -> {
            if (isGame) {
                startGame(s, Constants.beginner);
            } else {
                startMatch(s, Constants.beginner);
            }
        });

        buttonMed.setOnAction(event -> {
            if (isGame) {
                startGame(s, Constants.medium);
            } else {
                startMatch(s, Constants.medium);
            }
        });

        buttonBig.setOnAction(event -> {
            if (isGame) {
                startGame(s, Constants.expert);
            } else {
                startMatch(s, Constants.expert);
            }
        });


        pane.getChildren().addAll(buttonSmall, buttonMed, buttonBig, error);


        return pane;
    }

    /*public int takeInt(String message) {
        TextInputDialog tid= new TextInputDialog(message);
        tid.getEditor().setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        }));

        Optional<String> answer = tid.showAndWait();

        return Integer.parseInt(answer.get());
    }*/

    private void startGame(Stage s, String diff) {
        ApiClient.GameView view = ApiClient.get().newGame(diff);
        switchTo(s, new GameBoardScene(view, name));
    }

    private void startMatch(Stage s, String diff) {
        try {
            ApiClient.JoinResponse response = ApiClient.get().joinQueue(diff);
            if (response.status().equals("WAITING")) {
                switchTo(s, new WaitingScene(name));
            } else if (response.status().equals("MATCHED")){
                ApiClient.MatchView view = ApiClient.get().getMatchState(response.matchID());
                switchTo(s, new MatchBoardScene(view, name));
            }
        } catch (ApiClient.ApiError e) {
            error.setText(e.getMessage());
        }
    }
}
