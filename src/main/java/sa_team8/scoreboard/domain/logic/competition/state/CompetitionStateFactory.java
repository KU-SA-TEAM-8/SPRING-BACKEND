package sa_team8.scoreboard.domain.logic.competition.state;

public class CompetitionStateFactory {

  public static CompetitionState from(CompetitionStateEnum stateEnum) {
    return switch (stateEnum) {
      case WAITING -> new CompetitionWaitingState();
      case RUNNING -> new CompetitionRunningState();
      case PAUSED -> new CompetitionPausedState();
      case CLOSED -> new CompetitionClosedState();
    };
  }
}
