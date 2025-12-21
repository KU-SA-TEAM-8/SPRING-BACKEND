package sa_team8.scoreboard.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sa_team8.scoreboard.domain.command.UpdateTeamCommand;
import sa_team8.scoreboard.global.exception.ApplicationException;

@DisplayName("Team 도메인 단위 테스트")
class TeamTest {
  @Test
  @DisplayName("UT-05: 기존 팀의 이름을 변경할 수 있다")
  void updateTeams_updateTeamName() {
    // given
    Competition competition = Competition.create(
        "대회",
        "공지",
        "설명",
        Instant.now(),
        100,
        true,
        false
    );

    Team team = Team.create("기존 팀", competition, 0);
    team.setId(UUID.randomUUID());

    UpdateTeamCommand command = new UpdateTeamCommand(
        team.getId(),
        "변경된 팀",
        0
    );

    // when
    competition.updateTeams(List.of(command));

    // then
    assertThat(team.getName()).isEqualTo("변경된 팀");
  }

  @Test
  @DisplayName("UT-06: 요청에 포함되지 않은 팀은 soft delete 처리된다")
  void updateTeams_softDeleteTeam() {
    // given
    Competition competition = Competition.create(
        "대회",
        "공지",
        "설명",
        Instant.now(),
        100,
        true,
        false
    );

    Team team1 = Team.create("팀1", competition, 0);
    team1.setId(UUID.randomUUID());

    Team team2 = Team.create("팀2", competition, 0);
    team2.setId(UUID.randomUUID());

    UpdateTeamCommand command = new UpdateTeamCommand(
        team1.getId(),
        "팀1",
        0
    );

    // when
    competition.updateTeams(List.of(command));

    // then
    assertThat(team2.getDeletedAt()).isNotNull();
    assertThat(team1.getDeletedAt()).isNull();
  }

  @Test
  @DisplayName("UT-07: 존재하지 않는 teamId 전달 시 예외 발생")
  void updateTeams_teamNotFound() {
    // given
    Competition competition = Competition.create(
        "대회", "공지", "설명",
        Instant.now(), 100, true, false
    );

    UUID randomId = UUID.randomUUID();
    UpdateTeamCommand command = new UpdateTeamCommand(
        randomId, "없는 팀", 0
    );

    // when / then
    assertThatThrownBy(() ->
        competition.updateTeams(List.of(command))
    )
        .isInstanceOf(ApplicationException.class)
        .hasMessageContaining("Team을 찾을 수 없습니다.");
  }


}