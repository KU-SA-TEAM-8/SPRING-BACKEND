package sa_team8.scoreboard.domain.logic.history;

import lombok.Getter;
import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.Manager;
import sa_team8.scoreboard.domain.entity.Team;
import sa_team8.scoreboard.domain.event.BaseEvent;

@Getter
public abstract class ScoreEvent extends BaseEvent {

  protected final Competition competition;
  protected final Team team;
  protected final Team againstTeam;
  protected final Manager manager;
  protected final int delta;
  protected final String reason;

  public abstract ScoreEventType getType();

  public ScoreEvent(Competition competition, Team team, Team againstTeam, Manager manager, int delta, String reason) {
    this.competition = competition;
    this.team = team;
    this.againstTeam = againstTeam;
    this.delta = delta;
    this.reason = reason;
    this.manager = manager;
  }
}
