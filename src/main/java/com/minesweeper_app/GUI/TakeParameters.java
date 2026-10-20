package com.minesweeper_app.GUI;

import java.util.Optional;

import com.minesweeper_app.BoardGeneration.Game;

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
            s.setScene(new BoardScene(new Game(9, 9, 10), name, Constants.beginner).createScreen(s));
        });

        buttonMed.setOnAction(event -> {
            s.setScene(new BoardScene(new Game(16, 16, 40), name, Constants.medium).createScreen(s));
        });

        buttonBig.setOnAction(event -> {
            s.setScene(new BoardScene(new Game(30, 16, 99), name, Constants.expert).createScreen(s));
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
}
