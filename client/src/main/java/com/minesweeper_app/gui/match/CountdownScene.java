package com.minesweeper_app.gui.match;

import com.minesweeper_app.api.ApiClient;
import com.minesweeper_app.gui.Screens;
import com.minesweeper_app.gui.board.MatchBoardScene;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CountdownScene extends Screens {
    private final ApiClient.MatchView view;
    private Label countdownLabel = new Label();
    private Timeline timeline;
    private Label errorLabel = new Label();

    public CountdownScene(String name, ApiClient.MatchView view) {
        super(name);
        this.view = view;
    }

    @Override
    public Parent createRoot(Stage s) {
        VBox box = new VBox(30);
        box.setAlignment(Pos.CENTER);
        Label stat = new Label("MATCH FOUND GET READY");
        Label opp = new Label("OPPONENT IS " + view.oppUsername());

        box.getChildren().addAll(stat, countdownLabel, opp, errorLabel);

        createCountdown();
        return box;
    }

    private void createCountdown() {
        int timeLeft = view.timeLeftUntilStart();
        int startSeconds = (int) Math.ceil(timeLeft / 1000.0);
        countdownLabel.setText(Integer.toString(startSeconds));
        pop();
        timeline = new Timeline();

        for (int i = startSeconds - 1; i > 0; i--) {
            int at = timeLeft - (i * 1000);
            int lambdaI = i;
            KeyFrame frame = new KeyFrame(Duration.millis(at), event -> {
                countdownLabel.setText(Integer.toString(lambdaI));
                pop();
            });
            timeline.getKeyFrames().add(frame);

        }
        KeyFrame frame = new KeyFrame(Duration.millis(timeLeft), event -> {
            try {
                ApiClient.MatchView matchView = ApiClient.get().getMatchState(view.id());

                switchTo(s, new MatchBoardScene(matchView, name));
            } catch(ApiClient.ApiError e) {
                errorLabel.setText(e.getMessage());
            }
        });
        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    private void pop() {
        ScaleTransition scale = new ScaleTransition(Duration.millis(350), countdownLabel);
        scale.setFromX(1.6);
        scale.setFromY(1.6);
        scale.setToX(1.0);
        scale.setToY(1.0);

        FadeTransition fade = new FadeTransition(Duration.millis(350), countdownLabel);
        fade.setFromValue(0.2);
        fade.setToValue(1.0);

        ParallelTransition transition = new ParallelTransition();
        transition.getChildren().addAll(scale, fade);

        transition.play();
    }
}
