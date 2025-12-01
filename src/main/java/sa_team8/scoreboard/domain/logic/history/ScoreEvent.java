package sa_team8.scoreboard.domain.logic.history;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;

@Getter
public abstract class ScoreEvent {
  protected final int delta;
  protected final String reason;
  protected final UUID targetTeamId;   // 없으면 null
  protected final String managerName;  // 없으면 null

  public abstract ScoreHistoryType getType();

  protected ScoreEvent(int delta, String reason, UUID targetTeamId, String managerName) {
    this.delta = delta;
    this.reason = reason;
    this.targetTeamId = targetTeamId;
    this.managerName = managerName;
  }

  /**
   * DB에 JSON으로 저장할 payload를 key-value로 만든다.
   * 여기서는 공통 필드만 넣고, 서브클래스에서 덮어써도 됨.
   */
  public Map<String, Object> toPayload() {
    Map<String, Object> payload = new HashMap<>();
    if (targetTeamId != null) {
      payload.put("targetTeamId", targetTeamId.toString());
    }
    if (managerName != null) {
      payload.put("managerName", managerName);
    }
    return payload;
  }
}
