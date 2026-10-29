package com.minesweeper_app.GUI;

import java.util.Optional;

import com.minesweeper_app.api.ApiClient;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TakeParameters extends Screens{
    public TakeParameters(String aName) {
        super(aName);
    }

    @Override 
    public Scene createScreen(Stage s) {
        VBox pane = new VBox(15);
        pane.setAlignment(Pos.CENTER);

        Button buttonSmall = new Button("Small");
        Button buttonMed = new Button("Middle");
        Button buttonBig = new Button("Big");

        buttonSmall.setOnAction(event -> {
            start(s, Constants.beginner);
        });

        buttonMed.setOnAction(event -> {
            start(s, Constants.medium);
        });

        buttonBig.setOnAction(event -> {
            start(s, Constants.expert);
        });


        pane.getChildren().addAll(buttonSmall, buttonMed, buttonBig);


        return new Scene(pane, Constants.width, Constants.height);
    }

    public int takeInt(String message) {
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
    }

    private void start(Stage s, String diff) {
        ApiClient.GameView view = ApiClient.get().newGame(diff);
        s.setScene(new BoardScene(view, name).createScreen(s));
    }
}
