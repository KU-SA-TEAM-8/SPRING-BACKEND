package sa_team8.scoreboard.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="team")
public class Team extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "competition_id", nullable = false)
  private Competition competition;

  @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
  private Score score;

  public Team(String name, Competition competition) {
    this.name = name;
    this.competition = competition;
  }

  public static Team create(String name, Competition competition){
    Team team = new Team(name, competition);
    Score score = Score.create(team, 0);
    team.score = score;
    return team;
  }

  public void update(String name){
    this.name = name;
  }

}
