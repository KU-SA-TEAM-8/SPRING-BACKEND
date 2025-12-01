package sa_team8.scoreboard.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionMetaData {
  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String announcement;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private Instant startTime;

  @Column(nullable = false)
  private Integer totalTime;
}
