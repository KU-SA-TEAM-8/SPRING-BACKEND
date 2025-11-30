package sa_team8.scoreboard.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "score_board")
public class ScoreBoard extends BaseEntity {

  @Id
  @GeneratedValue
  private UUID id;

  // Competition FK (주인)
  @OneToOne
  @JoinColumn(name = "competition_id", nullable = false)
  private Competition competition;

  private ScoreBoard(Competition competition) {
    this.competition = competition;
  }

  public static ScoreBoard create(Competition competition) {
    return new ScoreBoard(competition);
  }
}
