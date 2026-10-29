package com.minesweeper_app.GUI;

import com.minesweeper_app.api.ApiClient;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AllTimesScene extends Screens{
    public AllTimesScene(String aName) {
        super(aName);
    }

    @Override 
    public Scene createScreen(Stage s) {
        ChoiceBox<String> diffBox = new ChoiceBox<>();
        diffBox.getItems().addAll(Constants.beginner, Constants.medium, Constants.expert);

        ListView<String> lView = new ListView<>();

        diffBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            lView.getItems().clear();
            for (int t : ApiClient.get().myTimes(newVal)) {
                lView.getItems().add(t + "s");
            }
        });

        diffBox.setValue(Constants.expert);

        Button back = new Button("Go back.");

        back.setOnAction(event -> {
            s.setScene(new MainMenu(name).createScreen(s));
        });

        BorderPane pane = new BorderPane();
        pane.setTop(diffBox);
        pane.setCenter(lView);
        pane.setBottom(back);

        return new Scene(pane, Constants.width, Constants.height);
    }
}
