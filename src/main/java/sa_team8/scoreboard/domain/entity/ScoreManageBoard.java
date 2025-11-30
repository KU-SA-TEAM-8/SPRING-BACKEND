package sa_team8.scoreboard.domain.entity;

import jakarta.persistence.Column;
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
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Table(name = "score_manage_board")
public class ScoreManageBoard extends BaseEntity {

  @Id
  @GeneratedValue
  private UUID id;

  // Competition FK (주인)
  @OneToOne
  @JoinColumn(name = "competition_id", nullable = false)
  private Competition competition;

  @Column(nullable = false)
  private UUID managerId;

  protected ScoreManageBoard() {}

  private ScoreManageBoard(Competition competition, UUID managerId) {
    this.competition = competition;
    this.managerId = managerId;
  }

  public static ScoreManageBoard create(Competition competition, UUID managerId) {
    return new ScoreManageBoard(competition, managerId);
  }
}
