package sa_team8.scoreboard.domain.logic.score;

import java.util.UUID;
import sa_team8.scoreboard.domain.entity.Score;
import sa_team8.scoreboard.domain.logic.history.ScoreEvent;

public interface ScoreObserver {
  void onScoreUpdated(ScoreEvent event);
}
