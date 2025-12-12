package sa_team8.scoreboard.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "manager_competition")
public class ManagerCompetition {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "manager_id", nullable = false)
  private Manager manager;

  @ManyToOne
  @JoinColumn(name = "competition_id", nullable = false)
  private Competition competition;

  public static ManagerCompetition createRelation(Manager manager, Competition competition) {
    ManagerCompetition create = ManagerCompetition.builder()
        .manager(manager)
        .competition(competition)
        .build();

    // 연관관계 관리
    manager.addManagerCompetitions(create);

    return create;
  }
}
