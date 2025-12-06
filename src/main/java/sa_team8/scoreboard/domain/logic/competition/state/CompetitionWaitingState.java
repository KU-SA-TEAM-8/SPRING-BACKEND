package sa_team8.scoreboard.domain.logic.competition.state;

import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.global.exception.ApplicationException;
import sa_team8.scoreboard.global.exception.ErrorCode;

public class CompetitionWaitingState implements CompetitionState {

  @Override
  public void start(Competition competition) {
    competition.changeState(new CompetitionRunningState());
  }

  @Override
  public void pause(Competition competition) {
    throw new ApplicationException(ErrorCode.INVALID_STATE_CHANGE, "대기 중인 대회는 일시정지 할 수 없습니다.");
  }

  @Override
  public void resume(Competition competition) {
    throw new ApplicationException(ErrorCode.INVALID_STATE_CHANGE, "대기 중인 대회는 재개할 수 없습니다.");
  }

  @Override
  public void close(Competition competition) {
    competition.changeState(new CompetitionClosedState());
  }

  @Override
  public CompetitionStateEnum getStateEnum() {
    return CompetitionStateEnum.WAITING;
  }
}
