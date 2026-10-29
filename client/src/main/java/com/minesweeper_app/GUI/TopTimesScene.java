package com.minesweeper_app.GUI;

import com.minesweeper_app.api.ApiClient;
import com.minesweeper_app.api.ApiClient.Entry;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TopTimesScene extends Screens{

    public TopTimesScene(String aName) {super(aName);}

    @Override
    public Scene createScreen(Stage s) {
        ChoiceBox<String> diffBox = new ChoiceBox<>();
        diffBox.getItems().addAll(Constants.beginner, Constants.medium, Constants.expert);

        TableView<Entry> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        TableColumn<Entry, String> usernameColumn = new TableColumn<>();
        usernameColumn.setText("Usernames");
        TableColumn<Entry, Integer> timeSecColumn = new TableColumn<>();
        timeSecColumn.setText("Times");

        table.getColumns().addAll(usernameColumn, timeSecColumn);

        usernameColumn.setCellValueFactory(f -> {
            Entry entry = f.getValue();
            String username = entry.username();
            return new SimpleStringProperty(username);
        });
        timeSecColumn.setCellValueFactory(f -> {
            Entry entry = f.getValue();
            int timeSec = entry.timeSec();
            return new SimpleIntegerProperty(timeSec).asObject();
        });

        diffBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            table.getItems().clear();
            table.getItems().setAll(ApiClient.get().topTimes(newVal));
        });

        diffBox.setValue(Constants.expert);

        Button backButton = new Button("Go back to main menu");

        backButton.setOnAction(e -> {
            s.setScene(new MainMenu(name).createScreen(s));
        });

        BorderPane pane = new BorderPane();
        pane.setTop(diffBox);
        pane.setCenter(table);
        pane.setBottom(backButton);

        return new Scene(pane, Constants.width, Constants.height);
    }
}
