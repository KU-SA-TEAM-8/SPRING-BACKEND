package sa_team8.scoreboard.presentation.competition.res;


import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.logic.competition.state.CompetitionStateEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCompetitionResponse {

  private UUID competitionId;
  private String name;
  private String announcement;
  private String description;
  private Instant startTime;
  private Integer totalTime;
  private CompetitionStateEnum state;
  private List<GetTeamResponse> teams;

  public static GetCompetitionResponse of(Competition competition) {
    List<GetTeamResponse> teams = competition.getTeams().stream()
        .map(team -> new GetTeamResponse(team.getId(), team.getName()))
        .toList();
    return new GetCompetitionResponse(
        competition.getId(),
        competition.getMetaData().getName(),
        competition.getMetaData().getAnnouncement(),
        competition.getMetaData().getDescription(),
        competition.getMetaData().getStartTime(),
        competition.getMetaData().getTotalTime(),
        competition.getStateEnum(),
        teams
    );
  }
}
