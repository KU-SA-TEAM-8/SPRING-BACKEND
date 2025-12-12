package sa_team8.scoreboard.presentation.score.res;

import java.util.UUID;
import sa_team8.scoreboard.domain.entity.Competition;

public record ScoreManageBoardListRes(
    UUID competitionId,
    String manageBoardPublicId,
    String competitionName
) {
  public static ScoreManageBoardListRes of(Competition competition) {
    return new ScoreManageBoardListRes(
        competition.getId(),
        competition.getScoreManageBoard().getPublicId(),
        competition.getMetaData().getName()
    );
  }
}
