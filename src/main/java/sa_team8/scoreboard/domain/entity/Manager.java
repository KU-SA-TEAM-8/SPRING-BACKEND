package sa_team8.scoreboard.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "manager")
public class Manager extends BaseEntity {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String password;   // 해시된 비밀번호

  @OneToMany(mappedBy = "manager")
  private List<ManagerCompetition> managerCompetitions = new ArrayList<>();

  private Manager(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  // UC-1.1 회원가입
  public static Manager create(String name, String email, String encodedPassword) {
    // validation 로직 추가
    return new Manager(name, email, encodedPassword);
  }

  public boolean isManagedCompetition(Competition competition) {
    return managerCompetitions.stream()
        .anyMatch(mc -> mc.getCompetition().equals(competition));
  }

  public void addManagerCompetitions(ManagerCompetition managerCompetition) {
    this.managerCompetitions.add(managerCompetition);
  }
}
