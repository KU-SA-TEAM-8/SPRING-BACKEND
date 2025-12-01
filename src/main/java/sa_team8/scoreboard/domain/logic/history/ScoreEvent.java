package sa_team8.scoreboard.domain.logic.history;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.Manager;
import sa_team8.scoreboard.domain.entity.Team;

@Getter
public abstract class ScoreEvent {

  protected final Competition competition;
  protected final Team team;
  protected final Team againstTeam;
  protected final Manager manager;
  protected final int delta;
  protected final String reason;

  public abstract ScoreEventType getType();
  public abstract void apply();

  public ScoreEvent(Competition competition, Team team, Team againstTeam, Manager manager, int delta, String reason) {
    this.competition = competition;
    this.team = team;
    this.againstTeam = againstTeam;
    this.delta = delta;
    this.reason = reason;
    this.manager = manager;
  }


  /**
   * DB에 JSON으로 저장할 payload를 key-value로 만든다.
   * 여기서는 공통 필드만 넣고, 서브클래스에서 덮어써도 됨.
   */
  public Map<String, Object> toPayload() {
    Map<String, Object> payload = new HashMap<>();
    payload.put("competitionId", competition.getId());
    payload.put("teamId", team.getId());
    payload.put("managerId", manager.getId());
    payload.put("delta", delta);
    payload.put("reason", reason);
    return payload;
  }
}
