package sa_team8.scoreboard.domain.logic.competition.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompetitionStateEnum {
  WAITING, RUNNING, PAUSED, CLOSED;
}
