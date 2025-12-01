package sa_team8.scoreboard.domain.logic.history;

import java.util.Map;

public class AdminAdjustScoreEvent extends ScoreEvent {

  public AdminAdjustScoreEvent(int delta, String reason, String managerName) {
    super(delta, reason, null, managerName);
  }

  @Override
  public ScoreHistoryType getType() {
    return ScoreHistoryType.ADMIN_ADJUSTMENT;
  }

  @Override
  public Map<String, Object> toPayload() {
    Map<String, Object> payload = super.toPayload();
    payload.put("eventDetail", "ADMIN_ADJUSTMENT");
    return payload;
  }
}
