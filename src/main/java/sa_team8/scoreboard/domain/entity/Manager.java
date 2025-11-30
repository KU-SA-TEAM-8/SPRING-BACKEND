package sa_team8.scoreboard.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="manager")
public class Manager extends BaseEntity {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String password;   // 해시된 비밀번호

  private Manager(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  // 정적 팩토리 메서드 (도메인 생성 규칙)
  public static Manager create(String name, String email, String encodedPassword) {
    return new Manager(name, email, encodedPassword);
  }

  // 비밀번호 인증 도메인 규칙
  public boolean authenticate(String encodedPassword) {
    return this.password.equals(encodedPassword);
  }

  public void updateProfile(String newName, String newEmail) {
    this.name = newName;
    this.email = newEmail;
  }
}
