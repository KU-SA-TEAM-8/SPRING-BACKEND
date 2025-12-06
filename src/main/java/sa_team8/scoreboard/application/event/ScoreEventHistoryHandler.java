package sa_team8.scoreboard.application.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import sa_team8.scoreboard.domain.entity.ScoreHistory;
import sa_team8.scoreboard.domain.logic.history.ScoreEvent;
import sa_team8.scoreboard.domain.repository.ScoreHistoryRepository;

@Component
@RequiredArgsConstructor
public class ScoreEventHistoryHandler {

  private final ScoreHistoryRepository historyRepo;

  @EventListener
  public void handleScoreEvent(ScoreEvent event) {
    ScoreHistory history = ScoreHistory.builder()
        .eventType(event.getType())
        .delta(event.getDelta())
        .reason(event.getReason())
        .competition(event.getCompetition())
        .targetTeam(event.getTeam())
        .againstTeam(event.getAgainstTeam())
        .manager(event.getManager())
        .build();

    historyRepo.save(history);
  }
}
