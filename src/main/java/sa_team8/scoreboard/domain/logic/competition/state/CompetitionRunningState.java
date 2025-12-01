package sa_team8.scoreboard.domain.logic.competition.state;

import sa_team8.scoreboard.domain.entity.Competition;

public class CompetitionRunningState implements CompetitionState {
  @Override
  public void start(Competition competition) {

  }

  @Override
  public void pause(Competition competition) {

  }

  @Override
  public void resume(Competition competition) {

  }

  @Override
  public void close(Competition competition) {

  }

  @Override
  public CompetitionStateEnum getStateEnum() {
    return CompetitionStateEnum.RUNNING;
  }
}
