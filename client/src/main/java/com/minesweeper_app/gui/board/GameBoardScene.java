package com.minesweeper_app.gui.board;

import com.minesweeper_app.gui.end.EndGameScene;
import com.minesweeper_app.api.ApiClient;

public class GameBoardScene extends AbstractBoardScene<ApiClient.GameView> {

    public GameBoardScene(ApiClient.GameView game, String aName) {
        super(game, aName);
    }

    @Override
    protected void play(boolean left, int row, int col) {
        try {
            if (left) {
                view = ApiClient.get().reveal(view.id(), row, col);
            } else {
                view = ApiClient.get().flag(view.id(), row, col);
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
    public void createEnd(int result) {
        switchTo(s, new EndGameScene(name, result, view));
    }
}
