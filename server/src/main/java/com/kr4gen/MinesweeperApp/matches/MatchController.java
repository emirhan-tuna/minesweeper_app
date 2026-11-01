package com.kr4gen.MinesweeperApp.matches;

import com.kr4gen.MinesweeperApp.auth.AuthInterceptor;
import com.kr4gen.MinesweeperApp.games.Difficulty;
import com.kr4gen.MinesweeperApp.games.GameDtos.gameRequest;
import com.kr4gen.MinesweeperApp.games.GameDtos.moveRequest;
import com.kr4gen.MinesweeperApp.matches.MatchDtos.MatchView;
import com.kr4gen.MinesweeperApp.user.User;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/matches")
public class MatchController {
    private final MatchService service;
    private final MatchRepository repository;

    public MatchController(MatchService service, MatchRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping("/queue")
    public MatchDtos.JoinResponse join(@RequestAttribute(AuthInterceptor.USER) User user, @Valid @RequestBody gameRequest request) {
        return service.join(user, request.diff());
    }

    @GetMapping("/queue")
    public MatchDtos.JoinResponse queueStatus(@RequestAttribute(AuthInterceptor.USER) User user) {
        return service.queueStatus(user);
    }

    @DeleteMapping("/queue")
    public MatchDtos.JoinResponse cancel(@RequestAttribute(AuthInterceptor.USER) User user) {
        return service.cancel(user);
    }

    @GetMapping("/{id}")
    public MatchView getState(@RequestAttribute(AuthInterceptor.USER) User user, @PathVariable String id) {
        return service.get(user, id);
    }

    @PostMapping("/{id}/reveal")
    public MatchView reveal(@RequestAttribute(AuthInterceptor.USER) User user, @PathVariable String id, @Valid@RequestBody moveRequest request) {
        return service.reveal(user, id, request.row(), request.col());
    }

    @PostMapping("/{id}/flag")
    public MatchView flag(@RequestAttribute(AuthInterceptor.USER) User user, @PathVariable String id, @Valid@RequestBody moveRequest request) {
        return service.flag(user, id, request.row(), request.col());
    }

    @PostMapping("/{id}/forfeit")
    public MatchView forfeit(@RequestAttribute(AuthInterceptor.USER) User user, @PathVariable String id) {
        return service.forfeit(user, id);
    }

    @GetMapping("/stats")
    public MatchDtos.Stats getResults(@RequestAttribute(AuthInterceptor.USER) User user, @RequestParam Difficulty diff) {
        return repository.getResults(user.id(), diff);
    }
}
