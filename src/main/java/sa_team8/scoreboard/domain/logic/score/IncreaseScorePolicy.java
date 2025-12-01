package sa_team8.scoreboard.domain.logic.score;

public class IncreaseScorePolicy implements ScorePolicy {

  // 싱글턴 인스턴스
  private static final IncreaseScorePolicy INSTANCE = new IncreaseScorePolicy();

  // private 생성자 (외부에서 생성 금지)
  private IncreaseScorePolicy() {
  }

  // 인스턴스 접근 메서드
  public static IncreaseScorePolicy getInstance() {
    return INSTANCE;
  }

  @Override
  public int apply(int currentValue, int delta) {
    return currentValue + delta;
  }
}
