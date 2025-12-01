package sa_team8.scoreboard.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sa_team8.scoreboard.domain.logic.score.ScoreObserver;
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

  @ManyToOne(optional = false)
  @JoinColumn(name = "team_id")
  private Team team;

  @Transient
  private ScorePolicy policy;

  @Transient
  private final List<ScoreObserver> observers = new ArrayList<>();

  private Score(Team team, int initialValue, ScorePolicy policy) {
    this.team = team;
    this.value = initialValue;
    this.policy = policy;
  }

  // ✨ 정적 팩토리 (도메인 생성 규칙)
  public static Score create(Team team, int initialValue, ScorePolicy policy) {
    return new Score(team, initialValue, policy);
  }

  // ---------------- Observer 관리 ----------------

  public void registerObserver(ScoreObserver observer) {
    this.observers.add(observer);
  }

  public void removeObserver(ScoreObserver observer) {
    this.observers.remove(observer);
  }

  private void notifyObservers(int delta, String reason, UUID managerId) {
    for (ScoreObserver observer : observers) {
      observer.onScoreUpdated(this, delta, reason, managerId);
    }
  }

  // ---------------- Domain Behavior ----------------

  /**
   * ScoreCommand.execute(score) 호출 시 실행될 메서드
   */
  public void applyChange(int delta, String reason, UUID managerId) {
    // 정책 적용 (로직은 나중에 구현)
    // int newValue = policy.apply(value, delta);
    // this.value = newValue;

    // 히스토리 & 브로드캐스트 알림
    notifyObservers(delta, reason, managerId);

    // TODO: 실제 점수 변경 로직
  }

}
