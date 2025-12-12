package sa_team8.scoreboard.domain.logic.score;

public enum ScorePolicyType {
  INCREASE {
    public ScorePolicy createPolicy() {
      return IncreaseScorePolicy.getInstance();
    }
  };

  abstract public ScorePolicy createPolicy();
}
