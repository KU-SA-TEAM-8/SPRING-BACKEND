package sa_team8.scoreboard.domain.logic.competition.state;

import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.global.exception.ApplicationException;
import sa_team8.scoreboard.global.exception.ErrorCode;

public class CompetitionClosedState implements CompetitionState {

  private static final String EXCEPTION_MESSAGE = "이미 종료된 대회는 상태를 변경할 수 없습니다.";

  @Override
  public void start(Competition competition) {
    throw new ApplicationException(ErrorCode.INVALID_STATE_CHANGE, EXCEPTION_MESSAGE);
  }

  @Override
  public void pause(Competition competition) {
    throw new ApplicationException(ErrorCode.INVALID_STATE_CHANGE, EXCEPTION_MESSAGE);
  }

  @Override
  public void resume(Competition competition) {
    throw new ApplicationException(ErrorCode.INVALID_STATE_CHANGE, EXCEPTION_MESSAGE);
  }

  @Override
  public void close(Competition competition) {
    throw new ApplicationException(ErrorCode.INVALID_STATE_CHANGE, EXCEPTION_MESSAGE);
  }

  @Override
  public CompetitionStateEnum getStateEnum() {
    return CompetitionStateEnum.CLOSED;
  }
}
