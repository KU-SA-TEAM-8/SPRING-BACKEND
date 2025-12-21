package sa_team8.scoreboard.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sa_team8.scoreboard.domain.logic.competition.state.CompetitionStateEnum;
import sa_team8.scoreboard.global.exception.ApplicationException;

@DisplayName("Competition 도메인 단위 테스트")
class CompetitionTest {
  @Test
  @DisplayName("UT-01: Competition 생성 시 초기 상태는 WAITING 이다")
  void createCompetition_initialStateWaiting() {
    // given
    String name = "테스트 대회";
    String announcement = "공지";
    String description = "설명";
    Instant startTime = Instant.now();
    Integer totalTime = 120;
    boolean isPublic = true;
    boolean isExternal = false;

    // when
    Competition competition = Competition.create(
        name,
        announcement,
        description,
        startTime,
        totalTime,
        isPublic,
        isExternal
    );

    // then
    assertThat(competition).isNotNull();
    assertThat(competition.getStateEnum()).isEqualTo(CompetitionStateEnum.WAITING);
  }

  @Test
  @DisplayName("UT-02: Competition 생성 시 ScoreBoard와 ScoreManageBoard가 자동 생성된다")
  void createCompetition_createBoards() {
    // when
    Competition competition = Competition.create(
        "대회",
        "공지",
        "설명",
        Instant.now(),
        100,
        true,
        false
    );

    // then
    assertThat(competition.getScoreBoard()).isNotNull();
    assertThat(competition.getScoreManageBoard()).isNotNull();
  }

  @Test
  @DisplayName("UT-03: updateScoreBoard 시 null 값은 기존 값을 유지한다")
  void updateScoreBoard_patchUpdate() {
    // given
    Competition competition = Competition.create(
        "원래 이름",
        "공지",
        "설명",
        Instant.now(),
        100,
        true,
        false
    );

    // when
    competition.updateScoreBoard(
        null,           // name
        null,
        null,
        null,
        150              // totalTime
    );

    // then
    CompetitionMetaData metaData = competition.getMetaData();
    assertThat(metaData.getName()).isEqualTo("원래 이름");
    assertThat(metaData.getTotalTime()).isEqualTo(150);
  }
  @Test
  @DisplayName("UT-08: WAITING 상태에서 start() 호출 시 RUNNING으로 전이")
  void startCompetition() {
    // given
    Competition competition = Competition.create(
        "대회", "공지", "설명",
        Instant.now(), 100, true, false
    );

    // when
    competition.start();

    // then
    assertThat(competition.getStateEnum())
        .isEqualTo(CompetitionStateEnum.RUNNING);
  }

  @Test
  @DisplayName("UT-09: WAITING 상태에서 pause() 호출 시 예외 발생")
  void pauseInvalidState() {
    // given
    Competition competition = Competition.create(
        "대회", "공지", "설명",
        Instant.now(), 100, true, false
    );

    // when / then
    assertThatThrownBy(competition::pause)
        .isInstanceOf(ApplicationException.class);
  }


}