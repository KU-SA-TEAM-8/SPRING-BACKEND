package sa_team8.scoreboard.domain.logic.history.factory;

import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.Manager;
import sa_team8.scoreboard.domain.entity.Team;
import sa_team8.scoreboard.domain.logic.history.NormalScoreEvent;
import sa_team8.scoreboard.domain.logic.history.ScoreEvent;
import sa_team8.scoreboard.domain.logic.history.ScoreEventType;

public class ScoreEventFactory {

  public static ScoreEvent create(ScoreEventType eventType, Competition competition, Team team, Manager manager, int delta,
      String reason) {
    return switch (eventType) {
      case NORMAL -> new NormalScoreEvent(competition, team, manager, delta, reason);
      default -> null;
    };
  }
}
