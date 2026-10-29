package com.kr4gen.MinesweeperApp.games;

import com.kr4gen.MinesweeperApp.auth.AuthInterceptor;
import com.kr4gen.MinesweeperApp.games.GameDtos.gameRequest;
import com.kr4gen.MinesweeperApp.games.GameDtos.gameView;
import com.kr4gen.MinesweeperApp.games.GameDtos.moveRequest;
import com.kr4gen.MinesweeperApp.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService service;

    public GameController(GameService games) {
        this.service = games;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public gameView start(@RequestAttribute(AuthInterceptor.USER) User user, @Valid@RequestBody gameRequest request) {
        return service.start(user, request.diff());
    }

    @GetMapping("/{id}")
    public gameView move(@RequestAttribute(AuthInterceptor.USER)User user, @PathVariable String id) {
        return service.get(user, id);
    }

    @PostMapping("/{id}/reveal")
    public gameView reveal(@RequestAttribute(AuthInterceptor.USER)User user, @PathVariable String id, @Valid@RequestBody moveRequest request) {
        return service.reveal(user, id, request.row(), request.col());
    }

    @PostMapping("/{id}/flag")
    public gameView flag(@RequestAttribute(AuthInterceptor.USER)User user, @PathVariable String id, @Valid@RequestBody moveRequest request) {
        return service.flag(user, id, request.row(), request.col());
    }
}
