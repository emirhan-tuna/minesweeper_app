package com.minesweeper_app.GUI;

import com.minesweeper_app.api.ApiClient;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MatchBoardScene extends AbstractBoardScene<ApiClient.MatchView> {
    private final ApiClient.MatchView matchView;
    private Timeline timeline;
    private boolean request;

    public MatchBoardScene(ApiClient.MatchView view, String aName) {
        super (view, aName);
        this.matchView = (ApiClient.MatchView) view;
    }

    @Override
    protected void play(boolean left, int row, int col) {
        try {
            if (left) {
                view = ApiClient.get().revealMatch(view.id(), row, col);
            } else {
                view = ApiClient.get().flagMatch(view.id(), row, col);
            }
        } catch (ApiClient.ApiError e) {
            error.setText(e.getMessage());
            return;
        }

        refresh();
        if (view.isOver()) {
            end();
        }
    }

    @Override
    protected void goBack(Stage s) {
        timeline.stop();
        try {
            view = ApiClient.get().forfeit(view.id());
        } catch (ApiClient.ApiError e) {
            error.setText(e.getMessage());
            switchTo(s, new MainMenu(name));
            return;
        }
        end();
    }

    @Override
    protected HBox setTopBar() {
        HBox top = super.setTopBar();
        Label oppUsernameLabel = new Label(matchView.oppUsername());
        top.getChildren().addAll(oppUsernameLabel);

        return top;
    }

    @Override
    protected void startPolling() {
        KeyFrame frame = new KeyFrame(Duration.seconds(1), event -> {
            poll();

        });
        timeline = new Timeline(frame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @Override
    protected void onGameEnded() {
        timeline.stop();
    }

    private void poll() {
        String id = matchView.id();
        if (request || ended) {
            return;
        }
        request = true;
        Task<ApiClient.MatchView> task = new Task<ApiClient.MatchView>() {
                @Override
                protected ApiClient.MatchView call() throws Exception {
                    return ApiClient.get().getMatchState(id);
                }
            };
        task.setOnSucceeded(event -> {
                view = task.getValue();
                refresh();
                if (view.isOver()) {
                    end();
                }
                request = false;
            });
        task.setOnFailed(event -> {
                error.setText(task.getException().getMessage());
                request = false;
            });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void createEnd(int result) {
        switchTo(s, new EndMatchScene(name, result, view));
    }
}
