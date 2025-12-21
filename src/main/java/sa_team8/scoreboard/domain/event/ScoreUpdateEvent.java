package sa_team8.scoreboard.domain.event;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sa_team8.scoreboard.domain.entity.Team;
import sa_team8.scoreboard.presentation.score.res.ScoreHistoryRes;

@Getter
@Builder
@AllArgsConstructor
public class ScoreUpdateEvent extends BaseEvent {
  private String publicId;
  private List<ScoreUpdateEventTeamRowRes> teams;
  private List<ScoreHistoryRes> scoreHistories;

  public record ScoreUpdateEventTeamRowRes(
      UUID teamId,
      String name,
      int score
  ) {

  }

  public static ScoreUpdateEvent create(String publicId, List<Team> teams, List<ScoreHistoryRes> scoreHistoryRes) {

    // 1) 팀 정보 매핑
    List<ScoreUpdateEventTeamRowRes> teamRows = teams.stream()
        .map(team -> new ScoreUpdateEventTeamRowRes(
            team.getId(),
            team.getName(),
            team.getScore() != null ? team.getScore().getValue() : 0
        ))
        .toList();

    // 2) 단일 히스토리를 리스트로 감싸기

    // 3) 이벤트 생성
    return ScoreUpdateEvent.builder()
        .publicId(publicId)
        .teams(teamRows)
        .scoreHistories(scoreHistoryRes)
        .build();
  }

}
