package sa_team8.scoreboard.domain.logic.score;

public interface ScorePolicy {
  int apply(int currentValue, int delta);
}
