package com.kr4gen.MinesweeperApp.matches;

import com.kr4gen.MinesweeperApp.games.Difficulty;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class MatchRepository {
    private final JdbcClient client;

    public MatchRepository(JdbcClient client) {
        this.client = client;
    }

    public void saveResult(String winnerID, String loserID, Difficulty diff) {
        client.sql("""
                        INSERT INTO match_stats(user_id, difficulty, wins, losses) VALUES (:winner, :difficulty, 1, 0),
                        (:loser, :difficulty, 0, 1) AS incoming
                        ON DUPLICATE KEY UPDATE 
                            wins = match_stats.wins + incoming.wins,
                            losses = match_stats.losses + incoming.losses""")
                .param("winner", winnerID)
                .param("loser", loserID)
                .param("difficulty", diff.name())
                .update();
    }
}
