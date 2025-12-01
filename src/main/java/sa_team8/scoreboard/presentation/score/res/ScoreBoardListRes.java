package sa_team8.scoreboard.presentation.score.res;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import sa_team8.scoreboard.domain.entity.CompetitionMetaData;
import sa_team8.scoreboard.domain.entity.ScoreBoard;

public record ScoreBoardListRes(
    List<ScoreBoardRow> scoreBoardRows,
    LocalDateTime nextCursorCreatedAt,
    UUID nextCursorId
) {
  public record ScoreBoardRow(
      String publicId,
      String name,
      Instant startTime,
      Integer totalTime
  ) {
    public static ScoreBoardRow from(ScoreBoard board) {
      CompetitionMetaData metaData = board.getCompetition().getMetaData();
      return new ScoreBoardRow(
          board.getPublicId(),
          metaData.getName(),
          metaData.getStartTime(),
          metaData.getTotalTime()
      );
    }
  }
}
