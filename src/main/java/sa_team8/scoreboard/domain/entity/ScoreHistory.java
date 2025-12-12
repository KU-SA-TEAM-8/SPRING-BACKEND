package sa_team8.scoreboard.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sa_team8.scoreboard.domain.logic.history.ScoreEvent;
import sa_team8.scoreboard.domain.logic.history.ScoreEventType;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "score_history")
public class ScoreHistory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ScoreEventType eventType;

  @Column(nullable = false)
  private int delta;

  @Column(nullable = false)
  private String reason;

  @ManyToOne
  @JoinColumn(name = "competition_id", nullable = false)
  private Competition competition;

  @ManyToOne
  @JoinColumn(name = "target_team_id", nullable = false)
  private Team targetTeam;

  @ManyToOne
  @JoinColumn(name = "against_team_id", nullable = true)
  private Team againstTeam;

  @ManyToOne
  @JoinColumn(name = "manager_id", nullable = false)
  private Manager manager;

  // DB는 JSON 컬럼 (MySQL: json, Postgres: jsonb 등)
  @Column(columnDefinition = "json")
  private String payloadJson;
}
