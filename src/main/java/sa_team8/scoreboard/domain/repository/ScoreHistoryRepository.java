package sa_team8.scoreboard.domain.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.ScoreHistory;

public interface ScoreHistoryRepository extends JpaRepository<ScoreHistory, UUID> {
  List<ScoreHistory> findByCompetitionOrderByCreatedAtDesc(Competition competition);
}
