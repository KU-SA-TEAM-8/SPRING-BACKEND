package sa_team8.scoreboard.domain.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.Team;

public interface TeamRepository extends JpaRepository<Team, UUID> {

  @EntityGraph(attributePaths = {"score"})
  List<Team> findJoinScoreAllByCompetition(Competition competition);

  @EntityGraph(attributePaths = {"score"})
  List<Team> findJoinScoreAllByCompetitionOrderByScore_ValueDesc(Competition competition);
}
