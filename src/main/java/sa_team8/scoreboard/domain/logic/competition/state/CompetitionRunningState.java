package sa_team8.scoreboard.domain.logic.competition.state;

import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.global.exception.ApplicationException;
import sa_team8.scoreboard.global.exception.ErrorCode;

public class CompetitionRunningState implements CompetitionState {
  @Override
  public void start(Competition competition) {
    throw new ApplicationException(ErrorCode.INVALID_STATE_CHANGE, "이미 진행 중인 대회는 시작할 수 없습니다.");
  }

  @Override
  public void pause(Competition competition) {
    competition.changeState(new CompetitionPausedState());
  }

  @Override
  public void resume(Competition competition) {
    throw new ApplicationException(ErrorCode.INVALID_STATE_CHANGE, "진행 중인 대회는 재개할 수 없습니다.");
  }

  @Override
  public void close(Competition competition) {
    competition.changeState(new CompetitionClosedState());
  }

  @Override
  public CompetitionStateEnum getStateEnum() {
    return CompetitionStateEnum.RUNNING;
  }
}
