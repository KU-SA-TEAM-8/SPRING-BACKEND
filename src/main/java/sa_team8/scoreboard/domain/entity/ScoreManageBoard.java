package sa_team8.scoreboard.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
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

  @OneToOne
  @JoinColumn(name = "competition_id", nullable = false)
  private Competition competition;

  @Column(nullable = false)
  private String publicId;

  @Column(nullable = false)
  private LocalDateTime lastAccessedAt;

  public static ScoreManageBoard create(Competition competition) {
    ScoreManageBoard board = new ScoreManageBoard();
    board.competition = competition;
    board.publicId = UUID.randomUUID().toString();
    board.lastAccessedAt = LocalDateTime.now();
    return board;
  }
}
