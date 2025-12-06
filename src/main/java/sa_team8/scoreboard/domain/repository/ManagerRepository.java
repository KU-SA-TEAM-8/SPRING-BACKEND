package sa_team8.scoreboard.domain.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sa_team8.scoreboard.domain.entity.Manager;

public interface ManagerRepository extends JpaRepository<Manager, UUID> {
    Optional<Manager> findByEmail(String email);

    @Query("SELECT m FROM Manager m LEFT JOIN FETCH m.managerCompetitions WHERE m.email = :email")
    Optional<Manager> findByEmailWithCompetitions(@Param("email") String email);
}
