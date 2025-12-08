package sa_team8.scoreboard.domain.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sa_team8.scoreboard.domain.command.UpdateTeamCommand;
import sa_team8.scoreboard.domain.logic.competition.state.CompetitionState;
import sa_team8.scoreboard.domain.logic.competition.state.CompetitionStateEnum;
import sa_team8.scoreboard.domain.logic.competition.state.CompetitionStateFactory;
import sa_team8.scoreboard.global.exception.ApplicationException;
import sa_team8.scoreboard.global.exception.ErrorCode;

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

  @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL)
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
      Instant startTime, Integer totalTime, Boolean isPublic, Boolean isExternal) {

      CompetitionMetaData metaData = new CompetitionMetaData(name, announcement, description, startTime, totalTime);
      //todo: metaData Validation
      Competition competition = new Competition();
      competition.metaData = metaData;
      competition.initializeBoards(isPublic, isExternal);
      competition.stateEnum = CompetitionStateEnum.WAITING;
      competition.setCompetitionState();

      return competition;
  }

  // UC-2.1
  public void initializeBoards(Boolean isPublic, Boolean isExternal) {
    this.scoreBoard = ScoreBoard.create(this, isPublic, isExternal);
    this.scoreManageBoard = ScoreManageBoard.create(this);
  }

  // UC-2.2
  public void updateScoreBoard(
      String name,
      String announcement,
      String description,
      Instant startTime,
      Integer totalTime
  ) {

    CompetitionMetaData current = this.metaData;

    // null이면 기존 값 유지
    String newName = (name != null) ? name : current.getName();
    String newAnnouncement = (announcement != null) ? announcement : current.getAnnouncement();
    String newDescription = (description != null) ? description : current.getDescription();
    Instant newStartTime = (startTime != null) ? startTime : current.getStartTime();
    Integer newTotalTime = (totalTime != null) ? totalTime : current.getTotalTime();

    // 새 MetaData로 교체
    this.metaData = new CompetitionMetaData(
        newName,
        newAnnouncement,
        newDescription,
        newStartTime,
        newTotalTime
    );
  }

  public void updateTeams(List<UpdateTeamCommand> commands) {

    Map<UUID, Team> oldTeams = this.teams.stream()
        .collect(Collectors.toMap(Team::getId, t -> t));

    // ADD + UPDATE
    for (UpdateTeamCommand cmd : commands) {

      // 신규 추가
      if (cmd.teamId() == null) {
        Team newTeam = Team.create(cmd.name(), this, cmd.initialScore());
        this.teams.add(newTeam);
        continue;
      }

      // 기존 팀
      Team existing = oldTeams.get(cmd.teamId());
      if (existing == null){
        throw new ApplicationException(ErrorCode.TEAM_NOT_FOUND);
      }

      // 이름 변경
      if (!existing.getName().equals(cmd.name())) {
        existing.update(cmd.name());
      }

      // 처리 완료된 팀 제거
      oldTeams.remove(cmd.teamId());
    }

    // 남은 oldTeams → 삭제해야 할 팀
    for (Team removed : oldTeams.values()) {
      removed.getScore().softDelete();
      removed.softDelete();
      this.teams.remove(removed);
    }
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
