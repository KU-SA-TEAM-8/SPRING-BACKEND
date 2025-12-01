package sa_team8.scoreboard.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sa_team8.scoreboard.domain.logic.score.ScorePolicy;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="score")
public class Score extends BaseEntity {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false)
  private int value;

  @OneToOne(optional = false)
  @JoinColumn(name = "team_id")
  private Team team;

  private Score(Team team, int initialValue ) {
    this.team = team;
    this.value = initialValue;
  }

  // 정적 팩토리 (도메인 생성 규칙)
  public static Score create(Team team, int initialValue) {
    return new Score(team, initialValue);
  }


  // ---------------- Domain Behavior ----------------
  public int applyChange(int delta, ScorePolicy policy) {
    // 정책 적용
    this.value = policy.apply(value, delta);
    return this.value;
  }
}
