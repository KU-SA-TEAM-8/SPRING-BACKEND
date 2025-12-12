package sa_team8.scoreboard.presentation.competition.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import java.time.Instant;

import java.util.List;
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
	@JsonSetter(nulls = Nulls.SKIP)
	private Instant startTime;
	@JsonSetter(nulls = Nulls.SKIP)
	private Integer totalTime;
	private List<UpdateTeamRequest> teams;
}
