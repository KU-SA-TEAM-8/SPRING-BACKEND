package sa_team8.scoreboard.presentation.competition.res;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetTeamResponse {
  private UUID teamId;
  private String name;
}
