package sa_team8.scoreboard.domain.event;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sa_team8.scoreboard.presentation.score.res.ScoreBoardRes;
import sa_team8.scoreboard.presentation.score.res.ScoreHistoryRes;

@Getter
@Builder
@AllArgsConstructor
public class CompetitionDataChangeEvent extends BaseEvent {
  private String publicId;
  private ScoreBoardRes scoreBoard;
  private List<ScoreHistoryRes> scoreHistories;
}
