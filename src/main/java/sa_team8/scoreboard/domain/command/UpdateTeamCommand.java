package sa_team8.scoreboard.domain.command;

import java.util.UUID;

public record UpdateTeamCommand(
    UUID teamId,
    String name,
    int initialScore
) {

}
