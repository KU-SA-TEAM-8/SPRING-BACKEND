package sa_team8.scoreboard.Infrastructure.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

  public static final String EXCHANGE = "scoreboard.exchange";

  public static final String SCORE_UPDATED_KEY = "score.updated";
  public static final String SCOREBOARD_UPDATED_KEY = "scoreboard.updated";

  @Bean
  public TopicExchange scoreboardExchange() {
    return new TopicExchange(EXCHANGE);
  }
}
