package sa_team8.scoreboard.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sa_team8.scoreboard.domain.logic.score.IncreaseScorePolicy;
import sa_team8.scoreboard.domain.logic.score.ScorePolicy;

@DisplayName("Score 도메인 단위 테스트")
class ScoreTest {

  @Test
  @DisplayName("UT-10: Score 생성 시 초기 점수가 설정된다")
  void createScore_initialValue() {
    // given
    Competition competition = Competition.create(
        "대회", "공지", "설명",
        Instant.now(), 100, true, false
    );
    Team team = Team.create("팀", competition, 0);

    // when
    Score score = Score.create(team, 10);

    // then
    assertThat(score.getValue()).isEqualTo(10);
    assertThat(score.getTeam()).isEqualTo(team);
  }

  @Test
  @DisplayName("UT-11: Score.applyChange 증가 정책 적용")
  void applyIncreasePolicy() {
    // given
    Competition competition = Competition.create(
        "대회", "공지", "설명",
        Instant.now(), 100, true, false
    );
    Team team = Team.create("팀", competition, 0);
    Score score = Score.create(team, 10);

    ScorePolicy policy = IncreaseScorePolicy.getInstance();

    // when
    int result = score.applyChange(5, policy);

    // then
    assertThat(result).isEqualTo(15);
    assertThat(score.getValue()).isEqualTo(15);
  }


  @Test
  @DisplayName("UT-12: Score 감소 적용")
  void applyDecreasePolicy() {
    // given
    Competition competition = Competition.create(
        "대회", "공지", "설명",
        Instant.now(), 100, true, false
    );
    Team team = Team.create("팀", competition, 0);
    Score score = Score.create(team, 3);

    ScorePolicy policy = IncreaseScorePolicy.getInstance();

    // when
    int result = score.applyChange(-3, policy);

    // then
    assertThat(score.getValue()).isEqualTo(result);
    assertThat(result).isGreaterThanOrEqualTo(0);
  }


  @Test
  @DisplayName("UT-13: Score softDelete 호출 시 deletedAt이 설정된다")
  void softDeleteScore() {
    // given
    Competition competition = Competition.create(
        "대회", "공지", "설명",
        Instant.now(), 100, true, false
    );
    Team team = Team.create("팀", competition, 0);
    Score score = Score.create(team, 10);

    // when
    score.softDelete();

    // then
    assertThat(score.getDeletedAt()).isNotNull();
  }

}