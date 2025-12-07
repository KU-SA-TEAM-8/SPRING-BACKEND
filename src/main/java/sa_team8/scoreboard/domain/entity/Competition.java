package sa_team8.scoreboard.domain.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sa_team8.scoreboard.domain.logic.competition.state.CompetitionState;
import sa_team8.scoreboard.domain.logic.competition.state.CompetitionStateEnum;
import sa_team8.scoreboard.domain.logic.competition.state.CompetitionStateFactory;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Table(name = "competition")
public class Competition extends BaseEntity {

  // Getter만 제공
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Embedded
  private CompetitionMetaData metaData;

  // ScoreBoard와의 1:1 (읽기 전용)
  @OneToOne(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true)
  private ScoreBoard scoreBoard;

  // ScoreManageBoard와의 1:1 (읽기 전용)
  @OneToOne(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true)
  private ScoreManageBoard scoreManageBoard;

  @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Team> teams = new ArrayList<>();

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CompetitionStateEnum stateEnum; // WAITING / RUNNING / PAUSED / CLOSED

  @Transient
  private CompetitionState state; // 실제 동작 객체 (State 패턴)

  @PostLoad
  private void onLoad() {
    setCompetitionState();
  }

  private void setCompetitionState() {
    this.state = CompetitionStateFactory.from(this.stateEnum);
  }


  // UC-2.1
  public static Competition create(String name, String announcement, String description,
      Instant startTime, Integer totalTime) {

      CompetitionMetaData metaData = new CompetitionMetaData(name, announcement, description, startTime, totalTime);

      Competition competition = new Competition();
      competition.metaData = metaData;
      competition.initializeBoards();
      competition.stateEnum = CompetitionStateEnum.WAITING;
      competition.setCompetitionState();

      return competition;
  }

  // UC-2.1
  public void initializeBoards() {
    this.scoreBoard = ScoreBoard.create(this);
    this.scoreManageBoard = ScoreManageBoard.create(this);
  }

  // UC-2.2
  public void updateScoreBoard(String name, String announcement, String description,
      Instant startTime, Integer totalTime) {

    CompetitionMetaData metaData = new CompetitionMetaData(name, announcement, description, startTime, totalTime);
    this.metaData = metaData;
  }

  // UC-2.1, 2.6
  public void addTeam(Team team) {
    this.teams.add(team);
  }

  public void removeTeam(Team team) {
    this.teams.remove(team);
  }

  // UC-2.4, 2.5
  public void changeState(CompetitionState newState) {
    this.state = newState;
    this.stateEnum = newState.getStateEnum();
  }

  // UC-2.4, 2.5
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
}
