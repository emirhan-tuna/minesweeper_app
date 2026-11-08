package com.minesweeper_app.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minesweeper_app.gui.board.BoardView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ApiClient {

    private static final ApiClient INSTANCE = new ApiClient(loadServerURL());

    public static ApiClient get() {
        return INSTANCE;
    }

    public record Entry(String username, int timeSec) {}
    public record Stats(int wins, int losses) {}
    public record AuthResponse(String token, String username) {}
    public record MatchView(String id, String diff, int width, int height, int minesLeft, String state, int elapsedSec, List<String> rows, String oppUsername, int timeLeftUntilStart) implements BoardView {
        public boolean isOver() {return state.equals("WIN") || state.equals("LOSE"); }
    }
    public record JoinResponse (String status, String matchID) {}

    public record GameView(String id, String diff, int width, int height, int minesLeft, String state, int elapsedSec, List<String> rows) implements BoardView{
        public boolean isOver() {
            return state.equals("WIN") || state.equals("LOSE");
        }
    }

    public static class ApiError extends RuntimeException {
        public final int status;

        public ApiError(int status, String message) {
            super(message);
            this.status = status;
        }
    }

    private final String baseUrl;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper json = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private String token;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public AuthResponse login(String username, String password) {
        AuthResponse r = send("POST", "api/auth/login", Map.of("username", username, "password", password), new TypeReference<>() {});
        token = r.token;
        return r;
    }

    public AuthResponse register(String username, String password) {
        AuthResponse r = send("POST", "api/auth/register", Map.of("username", username, "password", password), new TypeReference<>() {});
        token = r.token;
        return r;
    }

    public GameView newGame(String diff) {
        return send("POST", "api/games", Map.of("diff", diff), new TypeReference<>() {});
    }

    public JoinResponse joinQueue(String diff) {
        return send("POST", "api/matches/queue", Map.of("diff", diff), new TypeReference<>() {});
    }

    public JoinResponse getQueueState() {
        return send("GET", "api/matches/queue", null, new TypeReference<>() {});
    }

    public JoinResponse cancel() {
        return send("DELETE", "api/matches/queue", null, new TypeReference<>() {});
    }

    public MatchView getMatchState(String gameID) {
        return send("GET", "api/matches/" + gameID, null, new TypeReference<> () {});
    }

    public MatchView revealMatch(String gameID, int row, int col) {
        return send("POST", "api/matches/" + gameID + "/reveal", Map.of("row", row, "col", col), new TypeReference<>() {});
    }

    public MatchView flagMatch(String gameID, int row, int col) {
        return send("POST", "api/matches/" + gameID + "/flag", Map.of("row", row, "col", col), new TypeReference<>() {});
    }

    public MatchView forfeit(String gameID) {
        return send("POST", "api/matches/"+ gameID + "/forfeit", null, new TypeReference<>() {});
    }

    public GameView reveal(String gameID, int row, int col) {
        return send("POST", "api/games/" + gameID + "/reveal", Map.of("row", row, "col", col), new TypeReference<> () {});
    }

    public GameView flag(String gameID, int row, int col) {
        return send("POST", "api/games/" + gameID + "/flag", Map.of("row", row, "col", col), new TypeReference<>() {});
    }

    public List<Integer> myTimes(String diff) {
        return send("GET", "api/scores/me?diff=" + diff, Map.of("diff", diff), new TypeReference<>() {});
    }

    public List<Entry> topTimes(String diff) {
        return send("GET", "api/scores/top?diff=" + diff, Map.of("diff", diff), new TypeReference<>() {});
    }

    public Stats getResults(String diff) {
        return send("GET", "api/matches/stats?diff=" + diff, null, new TypeReference<>() {});
    }

    private <T> T send(String method, String path, Object body, TypeReference<T> type) {
        try {
            HttpRequest.Builder req= HttpRequest.newBuilder(URI.create(baseUrl + path))
                    .header("Content-Type", "application/json");
            if (token != null) {
                req.header("Authorization", "Bearer " + token);
            }

            if (body == null) {
                req.method(method, HttpRequest.BodyPublishers.noBody());
            } else {
                req.method(method, HttpRequest.BodyPublishers.ofString(json.writeValueAsString(body)));
            }

            HttpResponse<String> res = client.send(req.build(), HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() >= 400) {
                throw new ApiError(res.statusCode(), errorMessage(res));
            }

            if (res.body().isEmpty()) {
                return null;
            } else {
                return json.readValue(res.body(), type);
            }
        }
        catch (IOException e) {
            throw new ApiError(0, "Cant reach server");
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiError(0, "Interrupted");
        }
    }

    private String errorMessage(HttpResponse<String> res) {
        try {
            JsonNode node = json.readTree(res.body());
            return node.path("detail").asText("Error" + res.statusCode());
        }catch (IOException e) {
            return "Error" + res.statusCode();
        }
    }

    private static String loadServerURL() {
        Properties prop = new Properties();
        String fallback = "http://localhost:8080/";

        try (InputStream in = ApiClient.class.getResourceAsStream("/client.properties")){
            if (in != null) {
                prop.load(in);
            }
        } catch (IOException ignored) {
        }
        return prop.getProperty("server.url", fallback);
    }

}
