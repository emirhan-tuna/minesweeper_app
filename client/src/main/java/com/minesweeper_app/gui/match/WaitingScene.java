package com.minesweeper_app.gui.match;

import com.minesweeper_app.gui.Constants;
import com.minesweeper_app.gui.Screens;
import com.minesweeper_app.gui.menu.MainMenu;
import com.minesweeper_app.api.ApiClient;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WaitingScene extends Screens {
    private boolean request;
    private boolean left = false;
    private Timeline timeline;
    private Label error = new Label();

    final ApiClient client = ApiClient.get();
    public WaitingScene(String aName) {super(aName);}

    @Override
    public Parent createRoot(Stage s) {
        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        Label textLabel = new Label("Looking for a player");
        ProgressIndicator indicator = new ProgressIndicator();
        indicator.setMaxSize(50, 50);
        indicator.setMinSize(50, 50);

        Button cancelButton = new Button("Cancel");
        cancelButton.setMaxSize(Constants.buttonWidth, Constants.buttonHeight);
        cancelButton.setMinSize(Constants.buttonWidth, Constants.buttonHeight);

        cancelButton.setOnAction(event -> {
            handleCancel(s);
        });

        box.getChildren().addAll(error, textLabel,indicator, cancelButton);
        startPolling(s);
        return box;
    }

    private void startPolling(Stage s) {
        KeyFrame frame = new KeyFrame(Duration.seconds(2), event -> {
            poll(s);
        });
        timeline = new Timeline(frame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void poll(Stage s) {
        if (request) {
            return;
        }
        request = true;
        Task<ApiClient.JoinResponse> task = new Task<ApiClient.JoinResponse>() {
            @Override
            protected ApiClient.JoinResponse call() throws Exception {
                return ApiClient.get().getQueueState();
            }

        };
        task.setOnSucceeded(event -> {
            if (left) {
                return;
            }
            ApiClient.JoinResponse response = task.getValue();
            try {
                if (response.status().equals("MATCHED")) {
                    request = false;
                    ApiClient.MatchView view = ApiClient.get().getMatchState(response.matchID());
                    timeline.stop();
                    left = true;
                    switchTo(s, new CountdownScene(name, view));
                } else if (response.status().equals("WAITING")) {
                    request = false;
                } else {
                    request = false;
                    timeline.stop();
                    left = true;
                    switchTo(s, new MainMenu(name));
                }
            } catch (ApiClient.ApiError e) {
                error.setText(e.getMessage());
            }
        });
        task.setOnFailed(event -> {
            error.setText(task.getException().getMessage());
            request = false;
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void handleCancel(Stage s) {
        timeline.stop();
        try {
            ApiClient.JoinResponse response = ApiClient.get().cancel();
            if (response.status().equals("MATCHED")) {
                left = true;
                switchTo(s, new CountdownScene(name, ApiClient.get().getMatchState(response.matchID())));
            } else {
                left = true;
                switchTo(s, new MainMenu(name));
            }
        } catch (ApiClient.ApiError e) {
            left = true;
            error.setText(e.getMessage());
            switchTo(s, new MainMenu(name));
        }
    }
}
