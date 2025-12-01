package sa_team8.scoreboard.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sa_team8.scoreboard.domain.logic.history.ScoreEvent;
import sa_team8.scoreboard.domain.logic.history.ScoreHistoryType;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "score_history")
public class ScoreHistory extends BaseEntity {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false)
  private UUID scoreId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ScoreHistoryType eventType;

  @Column(nullable = false)
  private int delta;

  @Column(nullable = false)
  private String reason;

  @Column
  private UUID targetTeamId;

  @Column
  private String managerName;

  // DB는 JSON 컬럼 (MySQL: json, Postgres: jsonb 등)
  @Column(columnDefinition = "json")
  private String payloadJson;

  private ScoreHistory(
      UUID scoreId,
      ScoreHistoryType eventType,
      int delta,
      String reason,
      UUID targetTeamId,
      String managerName,
      String payloadJson
  ) {
    this.scoreId = scoreId;
    this.eventType = eventType;
    this.delta = delta;
    this.reason = reason;
    this.targetTeamId = targetTeamId;
    this.managerName = managerName;
    this.payloadJson = payloadJson;
  }

  // 도메인 이벤트 + 직렬화된 payload를 받아서 History 엔티티 생성
  public static ScoreHistory fromEvent(UUID scoreId, ScoreEvent event, String payloadJson) {
    return new ScoreHistory(
        scoreId,
        event.getType(),
        event.getDelta(),
        event.getReason(),
        event.getTargetTeamId(),
        event.getManagerName(),
        payloadJson
    );
  }

}
