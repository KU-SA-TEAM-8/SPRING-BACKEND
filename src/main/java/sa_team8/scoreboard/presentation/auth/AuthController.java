package sa_team8.scoreboard.presentation.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sa_team8.scoreboard.application.service.AuthService;
import sa_team8.scoreboard.presentation.auth.req.RefreshRequest;
import sa_team8.scoreboard.presentation.auth.req.SignInRequest;
import sa_team8.scoreboard.presentation.auth.req.SignUpRequest;
import sa_team8.scoreboard.presentation.auth.res.SignInResponse;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(
        @RequestBody SignUpRequest signUpRequest
    ) {
        authService.signUp(signUpRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(
        @RequestBody SignInRequest signInRequest
    ) {
        SignInResponse response = authService.signIn(signInRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        @RequestHeader("Authorization") String accessToken
    ) {
        authService.logout(accessToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<SignInResponse> refresh(
        @RequestBody RefreshRequest refreshRequest
    ) {
        SignInResponse response = authService.refresh(refreshRequest);
        return ResponseEntity.ok(response);
    }
}
