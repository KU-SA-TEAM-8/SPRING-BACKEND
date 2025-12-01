package sa_team8.scoreboard.domain.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import sa_team8.scoreboard.domain.entity.ManagerCompetition;

public interface ManagerCompetitionRepository extends JpaRepository<ManagerCompetition, UUID> {

}
