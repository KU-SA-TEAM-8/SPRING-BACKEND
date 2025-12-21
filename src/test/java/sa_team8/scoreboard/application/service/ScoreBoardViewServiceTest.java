package sa_team8.scoreboard.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.CompetitionMetaData;
import sa_team8.scoreboard.domain.entity.Score;
import sa_team8.scoreboard.domain.entity.ScoreBoard;
import sa_team8.scoreboard.domain.entity.Team;
import sa_team8.scoreboard.domain.logic.competition.state.CompetitionStateEnum;
import sa_team8.scoreboard.domain.repository.ScoreBoardRepository;
import sa_team8.scoreboard.domain.repository.TeamRepository;
import sa_team8.scoreboard.presentation.score.res.ScoreBoardListRes.ScoreBoardRow;
import sa_team8.scoreboard.presentation.score.res.ScoreBoardRes;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScoreBoardViewService 단위 테스트")
class ScoreBoardViewServiceTest {

  @Mock
  ScoreBoardRepository scoreBoardRepo;

  @Mock
  TeamRepository teamRepo;

  @InjectMocks
  ScoreBoardViewService scoreBoardViewService;

  @Test
  @DisplayName("UT-15: 공개 점수판 조회 시 팀 점수가 내림차순으로 반환된다")
  void getPublicScoreBoard() {
    // given
    String publicId = "public-123";

    // Competition MetaData
    CompetitionMetaData metaData = new CompetitionMetaData(
        "대회명",
        "공지사항",
        "설명",
        Instant.now(),
        100
    );

    Competition competition = mock(Competition.class);
    when(competition.getMetaData()).thenReturn(metaData);
    when(competition.getStateEnum()).thenReturn(CompetitionStateEnum.RUNNING);

    // ScoreBoard
    ScoreBoard scoreBoard = mock(ScoreBoard.class);
    when(scoreBoard.getCompetition()).thenReturn(competition);

    when(scoreBoardRepo.findJoinCompetitionByPublicId(publicId))
        .thenReturn(Optional.of(scoreBoard));

    // Teams (점수 내림차순 가정)
    Team team1 = mock(Team.class);
    Team team2 = mock(Team.class);

    Score score1 = mock(Score.class);
    Score score2 = mock(Score.class);

    when(team1.getId()).thenReturn(UUID.randomUUID());
    when(team1.getName()).thenReturn("팀1");
    when(score1.getValue()).thenReturn(30);
    when(team1.getScore()).thenReturn(score1);

    when(team2.getId()).thenReturn(UUID.randomUUID());
    when(team2.getName()).thenReturn("팀2");
    when(score2.getValue()).thenReturn(10);
    when(team2.getScore()).thenReturn(score2);

    when(teamRepo.findJoinScoreAllByCompetitionOrderByScore_ValueDesc(competition))
        .thenReturn(List.of(team1, team2));

    // when
    ScoreBoardRes result =
        scoreBoardViewService.getPublicScoreBoard(publicId);

    // then
    assertThat(result.competitionName()).isEqualTo("대회명");
    assertThat(result.state()).isEqualTo(CompetitionStateEnum.RUNNING);

    assertThat(result.teams()).hasSize(2);
    assertThat(result.teams().get(0).score()).isEqualTo(30);
    assertThat(result.teams().get(1).score()).isEqualTo(10);
  }

}
