package com.minesweeper_app.gui.menu;

import com.minesweeper_app.gui.*;
import com.minesweeper_app.gui.stats.AllTimesScene;
import com.minesweeper_app.gui.stats.MatchInfoScene;
import com.minesweeper_app.gui.stats.TopTimesScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu extends Screens {
    public MainMenu(String aName) {
        super(aName);
    }

    @Override 
    public Parent createRoot(Stage stage) {
        BorderPane layout = new BorderPane();
        VBox butBox = new VBox(20);
        butBox.setAlignment(Pos.CENTER);
        Label title = new Label("MINESWEEPER");
        title.setStyle("-fx-font-size: 36; -fx-font-weight: bold;");
        BorderPane.setAlignment(title, Pos.TOP_CENTER);
        BorderPane.setMargin(title, new Insets(40,0,0,0));

        Button startGame = new Button("Start game");
        startGame.setPrefSize(Constants.buttonWidth, Constants.buttonHeight);
        Button findMatch = new Button("Find an online match");
        findMatch.setPrefSize(Constants.buttonWidth, Constants.buttonHeight);
        Button personalTimes = new Button("See all the recorded times");
        personalTimes.setPrefSize(Constants.buttonWidth, Constants.buttonHeight);
        Button topTimes = new Button("See the top times");
        topTimes.setPrefSize(Constants.buttonWidth, Constants.buttonHeight);
        Button matchResults = new Button("See your match results");
        matchResults.setPrefSize(Constants.buttonWidth, Constants.buttonHeight);

        startGame.setOnAction(event -> {
            switchTo(stage, new TakeParameters(name, true));
        });
        findMatch.setOnAction(event -> {
            switchTo(stage, new TakeParameters(name, false));
        });
        personalTimes.setOnAction(event -> {
            switchTo(s, new AllTimesScene(name));
        });
        topTimes.setOnAction(event -> {
            switchTo(s, new TopTimesScene(name));
        });
        matchResults.setOnAction(event -> {
            switchTo(s, new MatchInfoScene(name));
        });

        butBox.getChildren().addAll(startGame, findMatch, personalTimes, topTimes, matchResults);
        layout.setTop(title);
        layout.setCenter(butBox);

        return layout;
    } 
}

