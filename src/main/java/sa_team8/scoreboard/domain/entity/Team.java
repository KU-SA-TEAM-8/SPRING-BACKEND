package sa_team8.scoreboard.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
  @GeneratedValue
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private int orderNumber; // ScoreBoard에 표시될 팀 순서

  @OneToOne
  @JoinColumn(name = "competition_id", nullable = false)
  private Competition competition;

  private Team(String name, int orderNumber, Competition competition) {
    this.name = name;
    this.orderNumber = orderNumber;
    this.competition = competition;
  }

  // ✨ 정적 팩토리 (도메인 규칙 정하는 곳)
  public static Team create(String name, int orderNumber, Competition competition) {
    return new Team(name, orderNumber, competition);
  }

  // ✨ 도메인 행위 (메서드 로직은 비움)
  public void changeName(String newName) {
    // TODO: 팀명 변경 규칙
  }

  public void changeOrder(int newOrder) {
    // TODO: 순서 변경 규칙
  }
}
