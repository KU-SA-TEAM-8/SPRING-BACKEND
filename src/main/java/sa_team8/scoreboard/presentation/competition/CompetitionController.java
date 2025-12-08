package sa_team8.scoreboard.presentation.competition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.UUID;

import org.springframework.http.HttpStatus;
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
import sa_team8.scoreboard.presentation.competition.req.CompetitionActionMode;
import sa_team8.scoreboard.presentation.competition.req.CreateCompetitionRequest;
import sa_team8.scoreboard.presentation.competition.req.UpdateCompetitionRequest;
import sa_team8.scoreboard.presentation.competition.res.CreateCompetitionResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/competitions")
public class CompetitionController {

	private final CompetitionService competitionService;

	// UC 2.1
	@Operation(
			summary = "대회 생성",
			description = """
            새로운 대회를 생성합니다.
            - 현재 로그인한 Manager가 대회의 관리자(ManagerCompetition)로 등록됩니다.
            - 팀은 두개 이상이여야 합니다.
            """
	)
	@PostMapping
	public ResponseEntity<CreateCompetitionResponse> createCompetition(
		@RequestBody CreateCompetitionRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(competitionService.createCompetition(request));
	}

	// UC 2.2, 2.6
	@Operation(
			summary = "대회 정보 수정",
			description = """
            기존 대회의 정보를 수정합니다. (PATCH 방식)
            - 누락된 필드는 기존 값이 유지됩니다.
            - 필드 단위의 부분 업데이트를 지원합니다.
            - Team List 는 기본적으로 존재하는 모든 팀을 입력하셔야 합니다.
            - 변경하지 않을 팀의 경우 teamId와 name 값을 주셔야 합니다.
            - 이름만 변경할 경우 teamId와 변경할 name 값을 주셔야 합니다.
            - List 에서 없어진 팀의 경우 삭제되고, 기존에 없던 팀을 주실 경우 생성됩니다.
            """
	)
	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateCompetition(
		@PathVariable UUID id,
		@RequestBody UpdateCompetitionRequest request
	) {
		competitionService.updateCompetition(id, request);
		return ResponseEntity.ok().build();
	}

	// UC 2.4, 2.5
	@Operation(
			summary = "대회 상태 변경",
			description = """
            대회의 상태를 변경합니다.
            """
	)
	@PostMapping("/{id}/actions")
	public ResponseEntity<Void> updateCompetitionAction(
		@PathVariable UUID id,
			@Parameter(
					description = "요청할 상태 전환 모드 (start, pause, resume, close)",
					required = true
			)
		@RequestParam("mode") CompetitionActionMode actionMode
	){
		competitionService.updateCompetitionAction(id, actionMode);
		return ResponseEntity.ok().build();
	}
}
