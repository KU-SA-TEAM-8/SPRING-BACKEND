package sa_team8.scoreboard.domain.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.ScoreHistory;

public interface ScoreHistoryRepository extends JpaRepository<ScoreHistory, UUID> {
  List<ScoreHistory> findByCompetitionOrderByCreatedAtDesc(Competition competition);

  @Query("SELECT h FROM ScoreHistory h " +
      "JOIN FETCH h.targetTeam " +
      "LEFT JOIN FETCH h.againstTeam " +
      "WHERE h.competition = :competition ORDER BY h.createdAt DESC")
  List<ScoreHistory> findWithTeamsByCompetitionOrderByCreatedAtDesc(@Param("competition") Competition competition);
}
