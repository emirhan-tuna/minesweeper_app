package com.kr4gen.MinesweeperApp.matches;

import com.kr4gen.MinesweeperApp.auth.AuthInterceptor;
import com.kr4gen.MinesweeperApp.games.GameDtos;
import com.kr4gen.MinesweeperApp.games.GameDtos.gameRequest;
import com.kr4gen.MinesweeperApp.games.GameDtos.gameView;
import com.kr4gen.MinesweeperApp.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
public class MatchController {
    private final MatchService service;

    public MatchController(MatchService service) {
        this.service = service;
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
}
