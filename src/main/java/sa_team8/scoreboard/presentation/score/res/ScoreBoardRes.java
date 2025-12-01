package sa_team8.scoreboard.presentation.score.res;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ScoreBoardRes(
    String competitionName,
    String announcement,
    String description,
    Instant startTime,
    Integer totalTime,
    List<ScoreBoardTeamRow> teams
) {

  public record ScoreBoardTeamRow(
      UUID teamId,
      String name,
      int score
  ) {

  }
}
