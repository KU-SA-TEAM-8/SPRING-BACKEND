package sa_team8.scoreboard.presentation.score.res;

import java.util.List;
import java.util.UUID;

// 점수 관리 보드 전체 응답
public record ScoreManageBoardRes(
    UUID competitionId,
    String competitionName,
    UUID managerId,
    String managerName,
    List<TeamScoreRow> teams
) {
  public record TeamScoreRow(
      UUID teamId,
      String teamName,
      int scoreValue
  ) {}
}
