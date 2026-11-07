package com.minesweeper_app.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Objects;

import static com.minesweeper_app.GUI.Constants.HIDDEN;
import static com.minesweeper_app.GUI.Constants.OPEN;

public abstract class AbstractEndScene extends Screens{

    private final Image flag = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/flag.png")));
    private final Image mine = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/mine.png")));
    private final Button[][] buttons;
    protected final int result;
    protected final BoardView view;
    protected Label resultLabel = new Label();

    public AbstractEndScene(String name, int result, BoardView resultPane) {
        super(name);
        this.result = result;
        this.view = resultPane;
        this.buttons = new Button[view.height()][view.width()];
    }

    @Override
    public Parent createRoot(Stage s) {
        GridPane resultPane = showEndGrid();
        BorderPane pane = new BorderPane();
        Button mainMenuButton = new Button("Go back to main menu");

        mainMenuButton.setOnAction(event -> {
            switchTo(s, new MainMenu(name));
        });

        resultLabel = setResultLabel();
        BorderPane.setAlignment(resultLabel, Pos.CENTER);
        BorderPane.setAlignment(mainMenuButton, Pos.CENTER);
        BorderPane.setMargin(resultLabel, new Insets(100, 20, 20, 20));
        BorderPane.setMargin(mainMenuButton, new Insets(20, 20, 100, 20));

        pane.setTop(resultLabel);
        pane.setCenter(resultPane);
        pane.setBottom(mainMenuButton);

        return pane;
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

    protected abstract Label setResultLabel();
}

