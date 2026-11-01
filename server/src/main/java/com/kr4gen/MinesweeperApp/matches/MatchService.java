package com.kr4gen.MinesweeperApp.matches;

import com.kr4gen.MinesweeperApp.api.ApiException;
import com.kr4gen.MinesweeperApp.games.Difficulty;
import com.kr4gen.MinesweeperApp.games.Game;
import com.kr4gen.MinesweeperApp.matches.MatchDtos.JoinResponse;
import com.kr4gen.MinesweeperApp.matches.MatchDtos.MatchView;
import com.kr4gen.MinesweeperApp.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final Object queueLock = new Object();

    private ConcurrentHashMap<Difficulty, User> queue = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Instant> lastActive = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, MatchSession> matches = new ConcurrentHashMap<>();

    public MatchService(MatchRepository repository) {
        this.matchRepository = repository;
    }

    public JoinResponse join(User user, Difficulty diff) {
        Instant now = Instant.now();
        synchronized (queueLock) {
            MatchSession session = matches.get(user.id());
            if (session != null) {
                if (session.isFinished()) {
                    matches.remove(user.id());
                } else {
                    return new JoinResponse(MatchDtos.JoinStatus.MATCHED, session.getID());
                }
            }

            for (var entry : queue.entrySet()) {
                if (entry.getValue().id().equals(user.id())) {
                    if (entry.getKey() == diff) {
                        lastActive.put(user.id(), now);
                        return new JoinResponse(MatchDtos.JoinStatus.WAITING, null);
                    } else {
                        throw new ApiException(HttpStatus.CONFLICT, "Already in other difficulty queue");
                    }
                }
            }

            User waitingPlayer = queue.get(diff);
            if (waitingPlayer == null) {
                lastActive.put(user.id(), now);
                queue.put(diff, user);
                return new JoinResponse(MatchDtos.JoinStatus.WAITING, null);
            } else {
                queue.remove(diff);
                MatchSession newSession = new MatchSession(waitingPlayer, user, diff);
                matches.put(waitingPlayer.id(), newSession);
                matches.put(user.id(), newSession);
                lastActive.remove(waitingPlayer.id());
                return new JoinResponse(MatchDtos.JoinStatus.MATCHED, newSession.getID());
            }
        }
    }

    public JoinResponse queueStatus(User user) {
        Instant now = Instant.now();
        synchronized (queueLock) {
            MatchSession curMatch = matches.get(user.id());
            if (curMatch != null) {
                if (!curMatch.isFinished()) {
                    lastActive.remove(user.id());
                    return new JoinResponse(MatchDtos.JoinStatus.MATCHED, curMatch.getID());
                }
            }

            for (var entry : queue.entrySet()) {
                if (entry.getValue().id().equals(user.id())) {
                    lastActive.put(user.id(), now);
                    return new JoinResponse(MatchDtos.JoinStatus.WAITING, null);
                }
            }
        }
        return new JoinResponse(MatchDtos.JoinStatus.NONE, null);
    }

    public JoinResponse cancel(User user) {
        synchronized (queueLock) {
            MatchSession session = matches.get(user.id());

            if (session != null && !session.isFinished()) {
                lastActive.remove(user.id());
                return new JoinResponse(MatchDtos.JoinStatus.MATCHED, session.getID());
            } else {
                for (var entry : queue.entrySet()) {
                    if (entry.getValue().id().equals(user.id())) {
                        queue.remove(entry.getKey());
                        lastActive.remove(user.id());
                        break;
                    }
                }
                return new JoinResponse(MatchDtos.JoinStatus.NONE, null);
            }
        }
    }

    public MatchView get(User user, String matchID) {
        Instant now = Instant.now();
        MatchSession session = find(user, matchID);

        synchronized (session) {
            session.setLastPlayed(now, user.id());
            return view(session, now, user);
        }
    }

    public MatchView reveal(User user, String id, int row, int col) {
        return move(user, id, row, col, game -> game.clickedLeft(row, col));
    }

    public MatchView flag(User user, String id, int row, int col) {
        return move(user, id, row, col, game -> game.clickedRight(row, col));
    }

    public MatchView forfeit(User user, String id) {
        Instant now =Instant.now();
        MatchSession session = find(user, id);

        synchronized (session) {
            if (!session.isFinished()) {
                session.finish(now, session.getOpponent(user.id()));
                matchRepository.saveResult(session.getOpponent(user.id()).id(), user.id(), session.getDiff());
            }

            return view(session, now, user);
        }

    }

    private MatchSession find(User user, String matchID) {
        MatchSession session = matches.get(user.id());

        if (session == null || !matchID.equals(session.getID())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Game not found");
        }

        return session;
    }

    private MatchView move(User user, String id, int row, int col, Consumer<Game> action) {
        Instant now = Instant.now();
        MatchSession session = find(user, id);

        synchronized (session) {
            Game game = session.getGame(user.id());

            if (game.notBounds(row, col)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Out of bounds move");
            }
            if (game.isOver() || session.isFinished()) {
                throw  new ApiException(HttpStatus.CONFLICT, "Game is already over");
            }

            action.accept(game);
            session.setLastPlayed(now, user.id());

            if (game.isOver() && session.getEnd() == null) {
                if (game.getStatus() == Game.GameState.WIN) {
                    session.finish(now, user);
                    matchRepository.saveResult(user.id(), session.getOpponent(user.id()).id(), session.getDiff());
                } else if (game.getStatus() == Game.GameState.LOSE) {
                    session.finish(now, session.getOpponent(user.id()));
                    matchRepository.saveResult(session.getOpponent(user.id()).id(), user.id(), session.getDiff());
                }
            }

            return view(session, now, user);
        }
    }

    private MatchView view(MatchSession session, Instant now, User user) {
        Game game = session.getGame(user.id());

        List<String> rows = new ArrayList<>();
        for (int i = 0; i < game.getHeight(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < game.getWidth(); j++) {
                if (game.isRevealed(i, j)) {
                    sb.append((char) ('0' + game.getMinesAround(i, j)));
                } else if ((game.isOver() || session.isFinished()) && game.isMine(i, j)) {
                    sb.append('*');
                } else if (game.isFlagged(i, j)) {
                    sb.append('F');
                } else {
                    sb.append('#');
                }
            }
            rows.add(sb.toString());
        }

        return new MatchView(session.getID(), session.getDiff(), game.getWidth(), game.getHeight(), game.getMines(), session.getGameState(user),
                session.gameDuration(now), rows, session.getOpponent(user.id()).username());
    }

    @Scheduled(fixedRate = 5000)
    public void cleanUp() {
        Instant now = Instant.now();
        for (var entry : matches.entrySet()) {
            MatchSession session = entry.getValue();
            synchronized (session) {
                if (!session.isFinished()) {
                    Optional<User> inactive = session.inactive(now);

                    if (inactive.isPresent()) {
                        User opp = session.getOpponent(inactive.get().id());
                        session.finish(now, opp);
                        matchRepository.saveResult(opp.id(), inactive.get().id(), session.getDiff());
                    }
                }
                if (session.isFinished()) {
                    Instant end = session.getEnd();
                    if (Duration.between(end, now).toSeconds() > Duration.ofSeconds(30).toSeconds()) {
                        matches.remove(entry.getKey(), session);
                    }
                }
            }
        }

        synchronized (queueLock) {
            for (var entry : queue.entrySet()) {
                User curUser = entry.getValue();
                Difficulty curDiff = entry.getKey();
                Instant lastSeen = lastActive.get(curUser.id());
                if (Duration.between(lastSeen, now).toSeconds() > Duration.ofSeconds(30).toSeconds()) {
                    queue.remove(curDiff, curUser);
                    lastActive.remove(curUser.id());
                }
            }
        }
    }
}
