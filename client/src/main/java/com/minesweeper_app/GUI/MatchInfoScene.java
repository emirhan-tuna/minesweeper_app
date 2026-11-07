package com.minesweeper_app.GUI;

import com.minesweeper_app.api.ApiClient;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MatchInfoScene extends Screens{

    private Label winNumber = new Label();
    private Label loseNumber = new Label();
    private Label percentageLabel = new Label();
    private Label errorLabel = new Label();

    public MatchInfoScene(String aName) {
        super(aName);
    }

    @Override
    public Parent createRoot(Stage s) {
        BorderPane pane = new BorderPane();
        ChoiceBox<String> diffBox = new ChoiceBox<>();
        diffBox.getItems().addAll(Constants.beginner, Constants.medium, Constants.expert);

        diffBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            try {
                ApiClient.Stats stats = ApiClient.get().getResults(newVal);
                winNumber.setText(Integer.toString(stats.wins()));
                loseNumber.setText(Integer.toString(stats.losses()));

                if (stats.wins() + stats.losses() == 0) {
                    percentageLabel.setText("No matches yet");
                } else {
                    double percentage = 100 * (stats.wins() * 1.0) / ((stats.losses() + stats.wins()) * 1.0);
                    percentageLabel.setText(String.format("Win percentage: %.1f", percentage));
                }
            } catch(ApiClient.ApiError e) {
                errorLabel.setText(e.getMessage());
            }
        });

        diffBox.setValue(Constants.expert);
        Button backButton = new Button("Go back to main menu");

        backButton.setOnAction(event -> {
            switchTo(s, new MainMenu(name));
        });
        HBox botBox = new HBox(20);
        botBox.setAlignment(Pos.CENTER);
        HBox midBox = new HBox(20);
        midBox.setAlignment(Pos.CENTER);
        midBox.getChildren().addAll(winNumber, loseNumber, percentageLabel);
        botBox.getChildren().addAll(backButton, errorLabel);
        pane.setTop(diffBox);
        pane.setCenter(midBox);
        pane.setBottom(botBox);

        return pane;
    }
}
