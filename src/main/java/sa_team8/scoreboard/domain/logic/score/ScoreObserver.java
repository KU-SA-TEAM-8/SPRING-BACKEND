package sa_team8.scoreboard.domain.logic.score;

import java.util.UUID;
import org.springframework.data.domain.Score;

public interface ScoreObserver {
  void onScoreUpdated(Score score, int delta, String reason, UUID managerId);
}
