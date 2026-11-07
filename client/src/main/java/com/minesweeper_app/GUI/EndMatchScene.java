package com.minesweeper_app.GUI;

import com.minesweeper_app.api.ApiClient;
import javafx.scene.control.Label;

public class EndMatchScene extends AbstractEndScene{

    private final ApiClient.MatchView matchView;

    public EndMatchScene(String name, int result, ApiClient.MatchView view) {
        super(name, result, view);
        this.matchView = view;
    }

    @Override
    public Label setResultLabel() {
        Label label = new Label();

        if (result == 0) {
            label.setText("You won against " + matchView.oppUsername() + ". Win has been saved.");
        } else {
            label.setText("You won against" + matchView.oppUsername() + ". Loss has been saved");
        }
        return label;
    }
}
