package sa_team8.scoreboard.domain.logic.history;

public class NormalScoreEvent extends ScoreEvent {

  public NormalScoreEvent(int delta, String reason) {
    super(delta, reason, null, null);
  }

  @Override
  public ScoreHistoryType getType() {
    return ScoreHistoryType.NORMAL;
  }
}
