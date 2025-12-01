package sa_team8.scoreboard.domain.logic.score;

import java.util.UUID;
import sa_team8.scoreboard.domain.entity.Score;

public interface ScoreObserver {
  void onScoreUpdated(Score score, int delta, String reason, UUID managerId);
}
