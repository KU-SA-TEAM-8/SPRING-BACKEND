package sa_team8.scoreboard.presentation.competition.req;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompetitionRequest {
	private String name;
	private String announcement;
	private String description;
	private Instant startTime;
	private Integer totalTime;
}
