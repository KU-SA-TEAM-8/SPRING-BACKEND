package sa_team8.scoreboard.domain.logic.history;

import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.Manager;
import sa_team8.scoreboard.domain.entity.Team;

public enum ScoreEventType {
  NORMAL {
    public ScoreEvent createEvent(
        Competition competition,
        Team team,
        Team againstTeam,
        Manager manager,
        int delta,
        String reason) {
      return new NormalScoreEvent(competition, team, manager, delta, reason);
    }
  },
  AGAINST_TEAM {
    public ScoreEvent createEvent(
        Competition competition,
        Team team,
        Team againstTeam,
        Manager manager,
        int delta,
        String reason) {
      return new NormalScoreEvent(competition, team, manager, delta, reason);
    }
  },      // A팀이 B팀에게 득점
  ADMIN_ADJUSTMENT {
    public ScoreEvent createEvent(
        Competition competition,
        Team team,
        Team againstTeam,
        Manager manager,
        int delta,
        String reason) {
      return new NormalScoreEvent(competition, team, manager, delta, reason);
    }
  },  // 관리자 수동 조정
  BONUS {
    public ScoreEvent createEvent(
        Competition competition,
        Team team,
        Team againstTeam,
        Manager manager,
        int delta,
        String reason) {
      return new NormalScoreEvent(competition, team, manager, delta, reason);
    }
  };             // 보너스 점수

  abstract public ScoreEvent createEvent(
      Competition competition, Team team, Team againstTeam, Manager manager, int delta,
      String reason);

}
