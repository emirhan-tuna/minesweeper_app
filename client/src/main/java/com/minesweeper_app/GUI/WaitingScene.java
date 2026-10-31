package com.minesweeper_app.GUI;

import com.minesweeper_app.api.ApiClient;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

public class WaitingScene extends Screens{

    final ApiClient client = ApiClient.get();
    public WaitingScene(String aName) {super(aName);}

    @Override
    public Scene createScreen(Stage s) {
        ProgressIndicator indicator = new ProgressIndicator();
        indicator.setMaxSize(50, 50);
        indicator.setMinSize(50, 50);

        Button cancelButton = new Button("Cancel");
        cancelButton.setMaxSize(Constants.buttonWidth, Constants.buttonHeight);
        cancelButton.setMinSize(Constants.buttonWidth, Constants.buttonHeight);

        cancelButton.setOnAction(event -> {
            client.cancel();
        });


    }
}
