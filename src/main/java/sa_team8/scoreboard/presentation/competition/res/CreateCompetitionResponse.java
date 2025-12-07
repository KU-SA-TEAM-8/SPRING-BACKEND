package sa_team8.scoreboard.presentation.competition.res;

import java.util.UUID;

public record CreateCompetitionResponse(
    UUID competitionId,
    String publicId
) {

}
