package com.minesweeper_app.GUI;

import com.minesweeper_app.api.ApiClient;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

class EndGameScene extends Screens{
    private static final String OPEN = "-fx-background-color: #dcdcdc; -fx-border-color: #a0a0a0; -fx-border-width: 0.5px; -fx-font-weight: bold; -fx-text-fill: ";
    private static final String HIDDEN = "-fx-background-color: #c0c0c0; -fx-border-color: #ffffff #7b7b7b #7b7b7b #ffffff; -fx-border-width: 2px; -fx-font-weight: bold;";
    private Image flag = new Image(getClass().getResourceAsStream("/images/flag.png"));
    private Image mine = new Image(getClass().getResourceAsStream("/images/mine.png"));
    private Button[][] buttons;
    private int result;
    private ApiClient.GameView view;

    public EndGameScene(String name, int result, ApiClient.GameView resultPane) {
        super(name);
        this.result = result;
        this.view = resultPane;
        this.buttons = new Button[view.height()][view.width()];
    }

    @Override 
    public Scene createScreen(Stage s) {
        GridPane resultPane = showEndGrid();
        BorderPane pane = new BorderPane();
        Label resultLabel = new Label();
        Button mainMenuButton = new Button("Go back to main menu");

        mainMenuButton.setOnAction(event -> {
            s.setScene(new MainMenu(name).createScreen(s));
        });
        if (result == 0) {
            resultLabel.setText("You won");
        } else {
            resultLabel.setText("You lose");
        }

        pane.setTop(resultLabel);
        pane.setCenter(resultPane);
        pane.setBottom(mainMenuButton);

        return new Scene(pane, Constants.width, Constants.height);
    }

    private GridPane showEndGrid() {
        GridPane resultPane = new GridPane();
        resultPane.setAlignment(Pos.CENTER);
        for (int i = 0; i < view.height(); i++) {
            for (int j = 0; j< view.width(); j++) {
                Button tile = new Button();
                tile.setPrefSize(30, 30);
                tile.setMaxSize(30, 30);
                tile.setMinSize(30, 30);

                buttons[i][j] = tile;
                resultPane.add(tile, j ,i);
            }
        }
        for (int i = 0; i < view.height(); i++) {
            for (int j = 0; j < view.width(); j++) {
                char c = view.rows().get(i).charAt(j);
                buttons[i][j].setGraphic(null);
                buttons[i][j].setText("");

                switch (c) {
                    case '#':
                        buttons[i][j].setStyle(HIDDEN);
                        break;
                    case 'F':
                        buttons[i][j].setStyle(HIDDEN);
                        buttons[i][j].setGraphic(new ImageView(flag));
                        break;
                    case '*':
                        buttons[i][j].setStyle(OPEN + "#ff0000");
                        buttons[i][j].setGraphic(new ImageView(mine));
                        break;
                    default:
                        int mines = c - '0';
                        buttons[i][j].setStyle(OPEN + Constants.getColor(mines));
                        if (mines > 0) {
                            buttons[i][j].setText(Integer.toString(mines));
                        }
                        break;
                }
            }
        }

        return resultPane;
    }
}