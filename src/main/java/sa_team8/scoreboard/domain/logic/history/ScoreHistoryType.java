package sa_team8.scoreboard.domain.logic.history;

public enum ScoreHistoryType {
  NORMAL,            // 그냥 득점
  AGAINST_TEAM,      // A팀이 B팀에게 득점
  ADMIN_ADJUSTMENT,  // 관리자 수동 조정
  BONUS,             // 보너스 점수
  PENALTY            // 패널티 점수
}
