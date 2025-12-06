package sa_team8.scoreboard.presentation.score;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sa_team8.scoreboard.application.service.ScoreManageService;
import sa_team8.scoreboard.presentation.score.req.ScoreChangeRequest;
import sa_team8.scoreboard.presentation.score.res.ScoreManageBoardRes;

@RestController
@RequestMapping("/api/v1/competitions/{competitionId}/managers/boards")
@RequiredArgsConstructor
public class ScoreManageController {

  private final ScoreManageService scoreManageService;

  @GetMapping
  public ResponseEntity<ScoreManageBoardRes> getScoreManageBoard(
      @PathVariable UUID competitionId
  ) {
    ScoreManageBoardRes response = scoreManageService.getScoreManageBoard(competitionId);
    return ResponseEntity.ok(response);
  }

  /**
   * UC-3.2 / 3.3 점수 증감 & 히스토리 기록 POST /scores/changes
   */
  @PatchMapping("/teams/{teamId}/scores")
  public ResponseEntity<Void> changeScore(
      @PathVariable UUID competitionId,
      @PathVariable UUID teamId,
      @RequestBody ScoreChangeRequest request
  ) {
    scoreManageService.changeScore(competitionId, teamId, request);
    return ResponseEntity.ok().build();
  }
}