package sa_team8.scoreboard.Infrastructure;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import sa_team8.scoreboard.domain.logic.history.ScoreEvent;

@Component
public class WebSocketBroadCaster {

  @EventListener
  public void handle(ScoreEvent event) {

  }
}
