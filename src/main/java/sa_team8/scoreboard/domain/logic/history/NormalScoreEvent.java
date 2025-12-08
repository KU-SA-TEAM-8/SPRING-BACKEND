package sa_team8.scoreboard.domain.logic.history;

import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.Manager;
import sa_team8.scoreboard.domain.entity.Team;

public class NormalScoreEvent extends ScoreEvent {

  public NormalScoreEvent(Competition competition, Team team, Manager manager, int delta, String reason) {
    super(competition, team, null, manager, delta, reason);
  }

  @Override
  public ScoreEventType getType() {
    return ScoreEventType.NORMAL;
  }
}
