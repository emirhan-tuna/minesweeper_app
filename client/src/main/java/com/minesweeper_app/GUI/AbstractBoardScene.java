package com.minesweeper_app.GUI;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public abstract class AbstractBoardScene<V extends BoardView> extends Screens{

    private static final String OPEN = "-fx-background-color: #dcdcdc; -fx-border-color: #a0a0a0; -fx-border-width: 0.5px; -fx-font-weight: bold; -fx-padding: 0; -fx-text-fill: ";
    private static final String HIDDEN = "-fx-background-color: #c0c0c0; -fx-border-color: #ffffff #7b7b7b #7b7b7b #ffffff; -fx-border-width: 2px;-fx-padding: 0; -fx-font-weight: bold;";

    protected V view;
    protected boolean ended = false;
    private Button[][] buttons;
    protected Label mineNumber = new Label();
    protected Label error = new Label();
    private Image flag = new Image(getClass().getResourceAsStream("/images/flag.png"));
    private Image mine = new Image(getClass().getResourceAsStream("/images/mine.png"));

    public AbstractBoardScene(V view, String aName) {
        super(aName);
        this.view = view;
        buttons = new Button[view.height()][view.width()];
    }

    @Override
    public Parent createRoot(Stage stage) {
        BorderPane layout = new BorderPane();
        this.s = stage;

        GridPane pane = showGrid();
        Button back = new Button("Back to menu");
        mineNumber = new Label(Integer.toString(view.minesLeft()));
        HBox top = setTopBar();


        back.setOnAction(event -> {
            goBack(stage);
        });

        layout.setTop(top);
        layout.setBottom(back);
        layout.setCenter(pane);

        refresh();
        startPolling();
        return layout;
    }

    protected GridPane showGrid(){
        GridPane resultPane = new GridPane();
        resultPane.setAlignment(Pos.CENTER);

        int row = view.height();
        int column = view.width();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                Button tile = new Button();
                tile.setPrefSize(30, 30);
                tile.setMinSize(30, 30);
                tile.setMaxSize(30, 30);
                int curRow = i;
                int curCol = j;

                tile.setOnMouseClicked((MouseEvent e) -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        play(true, curRow, curCol);
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        play(false, curRow, curCol);
                    }
                });

                buttons[i][j] = tile;
                resultPane.add(tile, j, i);
            }
        }

        return resultPane;
    }

    protected void refresh() {
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
                        buttons[i][j].setStyle(OPEN + "#ff0000");
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
                }
            }
        }

        mineNumber.setStyle("-fx-text-fill: " + Constants.getColor(0));
        mineNumber.setGraphic(new ImageView(flag));
        mineNumber.setText(Integer.toString(view.minesLeft()));
        mineNumber.setGraphicTextGap(10);
        mineNumber.setContentDisplay(ContentDisplay.RIGHT);
    }

    protected void goBack(Stage s) {
        switchTo(s, new MainMenu(name));
    }

    protected HBox setTopBar() {
        HBox top = new HBox(20);
        top.setStyle("-fx-background-color: #333333; -fx-padding: 10;");
        top.setAlignment(Pos.TOP_CENTER);
        Label nameLabel = new Label(name);
        top.getChildren().addAll(mineNumber, nameLabel, error);

        return top;
    }

    protected void startPolling() {}

    abstract void play(boolean left, int row, int col);

    protected void end() {
        onGameEnded();
        if (ended) {
            return;
        }
        this.ended = true;
        if (view.state().equals("WIN")) {
            createEnd(0);
        } else {
            createEnd(1);
        }
    }

    protected void onGameEnded() {}

    abstract void createEnd(int result);
}
