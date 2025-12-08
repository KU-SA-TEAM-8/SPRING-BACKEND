package sa_team8.scoreboard.application.service;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import sa_team8.scoreboard.domain.entity.Score;

public interface ScoreRepository extends JpaRepository<Score, UUID> {

}
