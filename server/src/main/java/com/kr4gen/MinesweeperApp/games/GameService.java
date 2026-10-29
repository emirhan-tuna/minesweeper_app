package com.kr4gen.MinesweeperApp.games;

import com.kr4gen.MinesweeperApp.api.ApiException;
import com.kr4gen.MinesweeperApp.games.GameDtos.gameView;
import com.kr4gen.MinesweeperApp.scores.ScoreRepository;
import com.kr4gen.MinesweeperApp.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service
public class GameService {

    private final Map<String, GameSession> gameByUsers = new ConcurrentHashMap<>();
    private ScoreRepository repository;

    public GameService(ScoreRepository repository) {
        this.repository = repository;
    }

    public gameView start(User user, Difficulty diff) {
        GameSession session = new GameSession(user.id(), diff);
        session.setStart(Instant.now());
        gameByUsers.put(user.id(), session);
        return view(session, Instant.now());
    }

    public gameView get(User user, String gameID) {
        GameSession session = find(user, gameID);

        synchronized (session) {
            return view(session, Instant.now());
        }
    }

    public gameView reveal(User user, String id, int row, int col) {
        return move(user, id, row, col, game -> game.clickedLeft(row, col));
    }

    public gameView flag(User user, String id, int row, int col) {
        return move(user, id, row, col, game -> game.clickedRight(row, col));
    }

    private GameSession find(User user, String gameID) {
        GameSession session = gameByUsers.get(user.id());

        if (session == null || !gameID.equals(session.id())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Game not found");
        }
        return session;
    }

    private gameView move(User user, String id, int row, int col, Consumer<Game> action) {
        GameSession session = find(user, id);

        synchronized (session) {
            Game game = session.game();

            if (game.notBounds(row, col)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Out of bounds");
            }
            if (game.isOver()) {
                throw new ApiException(HttpStatus.CONFLICT, "Game already over");
            }

            action.accept(game);

            if (game.getStatus() == Game.GameState.NOT_ON) {
                session.setStart(Instant.now());
            }
            if (game.isOver() && session.end() == null) {
                session.setEnd(Instant.now());
                if (game.getStatus() == Game.GameState.WIN) {
                    repository.save(user.id(), session.diff(), session.gameDuration(Instant.now()));
                }
            }

            return view(session, Instant.now());
        }
    }

    private gameView view(GameSession session, Instant now) {
        Game g = session.game();
        List<String> rows = new ArrayList<>(g.getHeight());
        for (int i = 0; i < g.getHeight(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < g.getWidth(); j++) {
                if (g.isRevealed(i, j)) {
                    sb.append((char)('0' + g.getMinesAround(i, j)));
                } else if (g.isOver() && g.isMine(i, j)) {
                    sb.append('*');
                } else if (g.isFlagged(i, j)) {
                    sb.append('F');
                } else {
                    sb.append('#');
                }
            }
            rows.add(sb.toString());
        }

        return new gameView(session.id(), session.diff(), g.getWidth(), g.getHeight(), g.getMines(), g.getStatus(), session.gameDuration(now), rows);
    }
}
