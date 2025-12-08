package sa_team8.scoreboard.domain.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import sa_team8.scoreboard.domain.entity.ScoreManageBoard;

public interface ScoreManageBoardRepository extends JpaRepository<ScoreManageBoard, UUID> {

  @EntityGraph(attributePaths = {"competition"})
  Optional<ScoreManageBoard> findJoinCompetitionByPublicId(String publicId);
}
