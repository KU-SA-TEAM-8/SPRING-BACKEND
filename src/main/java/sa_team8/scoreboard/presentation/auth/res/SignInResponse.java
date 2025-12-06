package sa_team8.scoreboard.presentation.auth.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sa_team8.scoreboard.global.security.jwt.JwtToken;

@Getter
@AllArgsConstructor
public class SignInResponse {
    private JwtToken jwtToken;
}
