package sa_team8.scoreboard.presentation.competition;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sa_team8.scoreboard.application.service.TeamService;
import sa_team8.scoreboard.presentation.competition.req.CreateCompetitionRequest;
import sa_team8.scoreboard.presentation.competition.req.CreateTeamRequest;
import sa_team8.scoreboard.presentation.competition.req.UpdateTeamRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/competitions")
public class TeamController {

	private final TeamService teamService;

	// UC 2.6
	@PostMapping("/{id}/teams")
	public ResponseEntity<Void> createTeam(
		@PathVariable UUID id,
		@RequestBody CreateTeamRequest request
	) {
		teamService.createTeam(id, request);
		return ResponseEntity.ok().build();
	}

	// UC 2.6
	@PatchMapping("/{id}/teams/{teamId}")
	public ResponseEntity<Void> updateTeam(
		@PathVariable UUID id,
		@PathVariable UUID teamId,
		@RequestBody UpdateTeamRequest request
	) {
		teamService.updateTeam(id, teamId, request);
		return ResponseEntity.ok().build();
	}

	// UC 2.6
	@DeleteMapping("/{id}/teams/{teamId}")
	public ResponseEntity<Void> deleteTeam(
		@PathVariable UUID id,
		@PathVariable UUID teamId
	) {
		teamService.deleteTeam(id, teamId);
		return ResponseEntity.ok().build();
	}

}
