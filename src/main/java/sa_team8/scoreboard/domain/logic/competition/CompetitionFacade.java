package sa_team8.scoreboard.domain.logic.competition;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.CompetitionMetaData;
import sa_team8.scoreboard.domain.logic.competition.state.CompetitionState;
import sa_team8.scoreboard.domain.logic.competition.state.CompetitionStateFactory;

@AllArgsConstructor
public class CompetitionFacade {

  private Competition competition;

  // UC-2.1
  public Competition createCompetition(UUID managerId, String name, String announcement, Instant startTime, Integer totalTime) {
    return null;
  }

  // UC-2.2
  public void updateCompetitionMeta(CompetitionMetaData newMeta) {
    competition.updateScoreBoard(newMeta);
  }

  // UC-2.4
  public void startCompetition() {
    CompetitionState state = CompetitionStateFactory.from(competition.getStateEnum());
    competition.changeState(state);
    competition.start();
  }

  // UC-2.5
  public void pauseCompetition() {
    competition.pause();
  }

  // UC-2.5
  public void resumeCompetition() {
    competition.resume();
  }

  // UC-2.6
  public void closeCompetition() {
    competition.close();
  }
}
