package sa_team8.scoreboard.domain.logic.competition.state;

import sa_team8.scoreboard.domain.entity.Competition;

public interface CompetitionState {

  void start(Competition competition);

  void pause(Competition competition);

  void resume(Competition competition);

  void close(Competition competition);

  CompetitionStateEnum getStateEnum();
}
