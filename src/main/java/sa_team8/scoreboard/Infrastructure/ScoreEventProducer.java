package sa_team8.scoreboard.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import sa_team8.scoreboard.Infrastructure.config.RabbitConfig;
import sa_team8.scoreboard.domain.event.CompetitionDataChangeEvent;
import sa_team8.scoreboard.domain.event.ScoreUpdateEvent;

@Component
@RequiredArgsConstructor
public class ScoreEventProducer {

  private final RabbitTemplate rabbitTemplate;

  @EventListener
  public void onScoreUpdated(ScoreUpdateEvent event) {
    rabbitTemplate.convertAndSend(
        RabbitConfig.EXCHANGE,
        RabbitConfig.SCORE_UPDATED_KEY,
        event
    );
  }

  @EventListener
  public void onScoreboardUpdated(CompetitionDataChangeEvent event) {
    rabbitTemplate.convertAndSend(
        RabbitConfig.EXCHANGE,
        RabbitConfig.SCOREBOARD_UPDATED_KEY,
        event
    );
  }
}
