package sa_team8.scoreboard.domain.logic.history;

import java.util.Map;
import java.util.UUID;

public class AgainstTeamScoreEvent extends ScoreEvent {

  public AgainstTeamScoreEvent(int delta, String reason, UUID targetTeamId) {
    super(delta, reason, targetTeamId, null);
  }

  @Override
  public ScoreHistoryType getType() {
    return ScoreHistoryType.AGAINST_TEAM;
  }

  @Override
  public Map<String, Object> toPayload() {
    Map<String, Object> payload = super.toPayload();
    payload.put("eventDetail", "AGAINST_TEAM");
    return payload;
  }
}
