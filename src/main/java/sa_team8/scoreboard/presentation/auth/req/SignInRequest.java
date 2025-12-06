package sa_team8.scoreboard.presentation.auth.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInRequest {
    private String email;
    private String password;
}
