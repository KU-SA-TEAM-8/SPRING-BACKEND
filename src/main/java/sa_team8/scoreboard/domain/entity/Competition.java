package sa_team8.scoreboard.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sa_team8.scoreboard.domain.logic.competition.state.CompetitionState;
import sa_team8.scoreboard.domain.logic.competition.state.CompetitionStateEnum;
import sa_team8.scoreboard.domain.logic.competition.state.CompetitionWaitingState;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Table(name = "competition")
public class Competition extends BaseEntity {

  // Getter만 제공
  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String announcement;

  @Column(nullable = false)
  private String description;

  // ScoreBoard와의 1:1 (읽기 전용)
  @OneToOne(mappedBy = "competition")
  private ScoreBoard scoreBoard;

  // ScoreManageBoard와의 1:1 (읽기 전용)
  @OneToOne(mappedBy = "competition")
  private ScoreManageBoard scoreManageBoard;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CompetitionStateEnum stateEnum; // WAITING / RUNNING / PAUSED / CLOSED

  @Transient
  private CompetitionState state; // 실제 동작 객체 (State 패턴)


  private Competition(
      String name,
      String announcement,
      String description
  ) {
    this.name = name;
    this.announcement = announcement;
    this.description = description;

    // 초기 상태
    this.state = new CompetitionWaitingState();
    this.stateEnum = state.getStateEnum();
  }

  // ✨ 정적 팩토리 메서드
  public static Competition create(
      UUID managerId,
      String name,
      String announcement,
      String description
  ) {
    return new Competition(managerId, name, announcement, description);
  }

  // 상태 변경 시 반영
  private void changeState(CompetitionState newState) {
    this.state = newState;
    this.stateEnum = newState.getStateEnum();
  }

  // Domain Behavior (State 패턴 위임)
  public void start() {
    this.state.start(this);
  }

  public void pause() {
    this.state.pause(this);
  }

  public void resume() {
    this.state.resume(this);
  }

  public void close() {
    this.state.close(this);
  }

  // State 구현 클래스가 Competition 내부의 changeState를 사용해야 하므로 공개
  public void setInternalState(CompetitionState newState) {
    this.changeState(newState);
  }
}
