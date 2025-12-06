package sa_team8.scoreboard.domain.logic.competition.state;

import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.global.exception.ApplicationException;
import sa_team8.scoreboard.global.exception.ErrorCode;

public class CompetitionPausedState implements CompetitionState {

  @Override
  public void start(Competition competition) {
    throw new ApplicationException(ErrorCode.INVALID_STATE_CHANGE, "일시정지된 대회는 시작할 수 없습니다.");
  }

  @Override
  public void pause(Competition competition) {
    throw new ApplicationException(ErrorCode.INVALID_STATE_CHANGE, "이미 일시정지된 대회입니다.");
  }

  @Override
  public void resume(Competition competition) {
    competition.changeState(new CompetitionRunningState());
  }

  @Override
  public void close(Competition competition) {
    competition.changeState(new CompetitionClosedState());
  }

  @Override
  public CompetitionStateEnum getStateEnum() {
    return CompetitionStateEnum.PAUSED;
  }
}
