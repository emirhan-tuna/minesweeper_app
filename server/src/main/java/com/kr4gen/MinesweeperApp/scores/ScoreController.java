package com.kr4gen.MinesweeperApp.scores;

import com.kr4gen.MinesweeperApp.auth.AuthInterceptor;
import com.kr4gen.MinesweeperApp.games.Difficulty;
import com.kr4gen.MinesweeperApp.scores.ScoreRepository.entry;
import com.kr4gen.MinesweeperApp.user.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    private final ScoreRepository repository;

    public ScoreController(ScoreRepository rep) {
        this.repository = rep;
    }

    @GetMapping("/me")
    public List<Integer> getMyTimes(@RequestAttribute(AuthInterceptor.USER)User user, @RequestParam Difficulty diff) {
        return repository.findTimes(user.id(), diff);
    }

    @GetMapping("/top")
    public List<entry> getTopTimes(@RequestParam Difficulty diff, @RequestParam(defaultValue = "10") int limit) {
        return repository.getTopTimes(diff, limit);
    }
}
