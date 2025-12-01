package sa_team8.scoreboard.domain.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import sa_team8.scoreboard.domain.entity.Competition;

public interface CompetitionRepository extends JpaRepository<Competition, UUID> {

  @EntityGraph(attributePaths = {"teams"})
  Optional<Competition> findJoinTeamById(UUID id);
}
