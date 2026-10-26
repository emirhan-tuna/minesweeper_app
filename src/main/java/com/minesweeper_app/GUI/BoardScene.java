package com.minesweeper_app.GUI;

import com.minesweeper_app.BoardGeneration.Game;
import com.minesweeper_app.DataBaseConnection.UserDao;

import javafx.geometry.Pos;
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

public class BoardScene extends Screens{
    Game game;
    private boolean first;
    private Button[][] buttons;
    private Label mineNumber;
    private String diff;
    private long startTime;
    private Image flag = new Image(getClass().getResourceAsStream("/images/flag.png"));

    public BoardScene(Game game, String aName, String aDiff) {
        super(aName);
        this.game = game;
        first = true;
        diff = aDiff;
        buttons = new Button[game.getHeight()][game.getWidth()];
    }

    @Override 
    public Scene createScreen(Stage stage) {
        BorderPane layout = new BorderPane();
        this.s = stage;

        GridPane pane = showGrid();
        Button back = new Button("Back to menu");
        mineNumber = new Label(Integer.toString(game.minesLeft()));
        Label nameLabel = new Label(name);
        HBox top = new HBox(20);
        top.setStyle("-fx-background-color: #333333; -fx-padding: 10;");

        top.setAlignment(Pos.TOP_CENTER);
        top.getChildren().addAll(mineNumber, nameLabel);

        back.setOnAction(event -> {
            stage.setScene(new MainMenu(name).createScreen(stage));
        });

        layout.setTop(top);
        layout.setBottom(back);
        layout.setCenter(pane);

        return new Scene(layout, Constants.width, Constants.height);
    } 

    public GridPane showGrid(){
        GridPane resultPane = new GridPane();
        resultPane.setAlignment(Pos.CENTER);

        int row = game.getHeight();
        int column = game.getWidth();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                String mines = Integer.toString(game.getMinesAround(i, j));
                if (mines.equals("0")) {
                    mines = "";
                }
                Button tile = new Button(mines);
                tile.setPrefSize(30, 30);
                tile.setMinSize(30, 30);
                tile.setMaxSize(30, 30);
                int curRow = i;
                int curCol = j;

                tile.setOnMouseClicked((MouseEvent e) -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        handleLeftClick(curRow, curCol);
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        handleRightClick(curRow, curCol);
                    } 
                });

                buttons[i][j] = tile;
                resultPane.add(tile, j, i);
            }
        }

        return resultPane;
    }

    public void handleLeftClick(int x, int y) {
        if (first) {
            game.startGame(x, y);
            first = false;
            startTime = System.currentTimeMillis();
        } else {
            game.clickedLeft(x, y);
        }

        if (!game.isActive()) {
            endGame(game.gameOver());
        }

        refresh();
    }

    public void handleRightClick(int x, int y) {
        game.clickedRight(x, y);

        if (!game.isActive()) {
            endGame(game.gameOver());
        }

        
        refresh();
    }

    public void refresh() {
        for (int i = 0; i < game.getHeight(); i++) {
            for (int j = 0; j < game.getWidth(); j++) {
                buttons[i][j].setGraphic(null);
                int mines = game.getMinesAround(i, j);
                if (game.isFlagged(i, j)) {
                    ImageView flagView = new ImageView(flag);
                    flagView.setFitWidth(20);
                    flagView.setFitHeight(20);
                    flagView.setPreserveRatio(true);
                    buttons[i][j].setGraphic(flagView);
                    buttons[i][j].setText("");
                } else if (game.isRevealed(i, j)) {
                    buttons[i][j].setText(Integer.toString(mines));
                    if (mines == 0) {
                        buttons[i][j].setText("");
                    }
                    buttons[i][j].setStyle("-fx-background-color: #dcdcdc; -fx-border-color: #a0a0a0; -fx-border-width: 0.5px; -fx-font-weight: bold; -fx-text-fill: " + Constants.getColor(mines));
                } else {
                    buttons[i][j].setStyle("-fx-background-color: #c0c0c0; -fx-border-color: #ffffff #7b7b7b #7b7b7b #ffffff;" + 
                        "-fx-border-width: 2px; -fx-font-weight: bold;"
                    );
                    buttons[i][j].setText("");
                }
            }
        }
        ImageView flagView = new ImageView(flag);
        flagView.setFitWidth(20);
        flagView.setFitHeight(20);
        flagView.setPreserveRatio(true);

        mineNumber.setStyle("-fx-text-fill: " + Constants.getColor(0));
        mineNumber.setGraphic(flagView);
        mineNumber.setText(Integer.toString(game.getMines()));
        mineNumber.setGraphicTextGap(10);
        mineNumber.setContentDisplay(ContentDisplay.RIGHT);
    }

    private void endGame(int win) {
        int seconds = (int) ((System.currentTimeMillis() - startTime) / 1000);
        if (win == 0) {
            UserDao.recordWin(name, diff, seconds);
        }
        s.setScene(new EndGameScene(this.name, game.gameOver()).createScreen(s));
    }
}
