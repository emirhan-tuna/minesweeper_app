package com.minesweeper_app.gui.stats;

import com.minesweeper_app.gui.Constants;
import com.minesweeper_app.gui.Screens;
import com.minesweeper_app.gui.menu.MainMenu;
import com.minesweeper_app.api.ApiClient;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AllTimesScene extends Screens {
    public AllTimesScene(String aName) {
        super(aName);
    }

    @Override 
    public Parent createRoot(Stage s) {
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
            switchTo(s, new MainMenu(name));
        });

        BorderPane pane = new BorderPane();
        pane.setTop(diffBox);
        pane.setCenter(lView);
        pane.setBottom(back);

        return pane;
    }
}
