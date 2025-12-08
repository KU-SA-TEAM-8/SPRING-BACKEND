package sa_team8.scoreboard.presentation.competition.req;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeamRequest {
	private UUID teamId;
	private String name;
	private int initialScore;
}