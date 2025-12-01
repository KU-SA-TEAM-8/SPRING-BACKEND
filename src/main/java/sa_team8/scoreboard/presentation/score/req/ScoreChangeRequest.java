package sa_team8.scoreboard.presentation.score.req;

import java.util.UUID;
import sa_team8.scoreboard.domain.logic.history.ScoreEventType;
import sa_team8.scoreboard.domain.logic.score.ScorePolicyType;

public record ScoreChangeRequest(
    UUID teamIdAgainst,
    int delta,
    String reason,
    ScoreEventType eventType,
    ScorePolicyType policyType
) {

}
