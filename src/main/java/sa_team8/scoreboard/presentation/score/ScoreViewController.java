package sa_team8.scoreboard.presentation.score;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sa_team8.scoreboard.application.service.ScoreBoardViewService;
import sa_team8.scoreboard.presentation.score.res.ScoreBoardListRes;
import sa_team8.scoreboard.presentation.score.res.ScoreBoardRes;
import sa_team8.scoreboard.presentation.score.res.ScoreHistoryRes;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/scoreboard")
public class ScoreViewController {

  private final ScoreBoardViewService scoreBoardViewService;

  @GetMapping("/{publicId}")
  public ResponseEntity<ScoreBoardRes> getScoreboard(@PathVariable String publicId) {
    ScoreBoardRes publicScoreBoard = scoreBoardViewService.getPublicScoreBoard(publicId);
    return ResponseEntity.ok(publicScoreBoard);
  }

  @GetMapping("/{publicId}/history")
  public ResponseEntity<List<ScoreHistoryRes>> getHistory(@PathVariable String publicId) {
    List<ScoreHistoryRes> history = scoreBoardViewService.getHistory(publicId);
    return ResponseEntity.ok(history);
  }

  @GetMapping("/list")
  public ResponseEntity<ScoreBoardListRes> list(
      @RequestParam(defaultValue = "30") Integer size,
      @RequestParam(required = false) LocalDateTime cursorCreatedAt,
      @RequestParam(required = false) UUID cursorId
  ) {
    ScoreBoardListRes scoreBoardListRes = scoreBoardViewService.listPublicScoreboards(
        cursorCreatedAt, cursorId, size);
    return ResponseEntity.ok(scoreBoardListRes);
  }
}
