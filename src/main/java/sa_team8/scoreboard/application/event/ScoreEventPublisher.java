package sa_team8.scoreboard.application.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import sa_team8.scoreboard.domain.event.BaseEvent;
import sa_team8.scoreboard.domain.logic.history.ScoreEvent;

@Component
public class ScoreEventPublisher {

  private static ApplicationEventPublisher publisher;

  @Autowired
  public ScoreEventPublisher(ApplicationEventPublisher p) {
    publisher = p;
  }

  public static void publish(BaseEvent event) {
    publisher.publishEvent(event);
  }
}
