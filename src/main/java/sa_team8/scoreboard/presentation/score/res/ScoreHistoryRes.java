package sa_team8.scoreboard.presentation.score.res;

import java.time.Instant;

public record ScoreHistoryRes(
    String teamName,
    String againstTeamName,
    int delta,
    String reason,
    Instant changedAt
) {

}
