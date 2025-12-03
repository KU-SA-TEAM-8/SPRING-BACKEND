package sa_team8.scoreboard.presentation.competition;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sa_team8.scoreboard.application.service.CompetitionService;
import sa_team8.scoreboard.presentation.competition.req.CreateCompetitionRequest;
import sa_team8.scoreboard.presentation.competition.req.UpdateCompetitionRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/competitions")
public class CompetitionController {

	private final CompetitionService competitionService;

	// UC 2.1
	@PostMapping
	public ResponseEntity<Void> createCompetition(
		@RequestBody CreateCompetitionRequest request
	) {
		competitionService.createCompetition(request);
		return (ResponseEntity<Void>)ResponseEntity.ok();
	}

	// UC 2.2
	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateCompetition(
		@PathVariable UUID id,
		@RequestBody UpdateCompetitionRequest request
	) {
		competitionService.updateCompetition(id, request);
		return (ResponseEntity<Void>)ResponseEntity.ok();
	}

	// UC 2.4
	@PostMapping("/{id}/actions")
	public ResponseEntity<Void> updateCompetitionAction(
		@PathVariable UUID id,
		@RequestParam("mode") String actionMode
	){
		competitionService.updateCompetitoinAction(id, actionMode);
		return (ResponseEntity<Void>)ResponseEntity.ok();
	}

	// UC 2.5
	@PostMapping("/{id}/restart")
	public ResponseEntity<Void> restartCompetition(
		@PathVariable UUID id,
		@RequestParam("mode") String mode
	){
		competitionService.restartCompetition(id, mode);
		return (ResponseEntity<Void>)ResponseEntity.ok();
	}

}
