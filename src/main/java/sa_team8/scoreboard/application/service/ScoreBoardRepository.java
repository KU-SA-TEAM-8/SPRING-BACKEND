package sa_team8.scoreboard.application.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sa_team8.scoreboard.domain.entity.ScoreBoard;

public interface ScoreBoardRepository extends JpaRepository<ScoreBoard, UUID> {

  @EntityGraph(attributePaths = {"competition"})
  Optional<ScoreBoard> findJoinCompetitionByPublicId(String publicId);

  @Query("""
    SELECT sb FROM ScoreBoard sb
    WHERE sb.isPublic = true
      AND (
           :cursorCreatedAt IS NULL
           OR
           (sb.createdAt < :cursorCreatedAt)
           OR
           (sb.createdAt = :cursorCreatedAt AND sb.id < :cursorId)
      )
    ORDER BY sb.createdAt DESC, sb.id DESC
    """)
  List<ScoreBoard> findNextPage(
      @Param("cursorCreatedAt") LocalDateTime cursorCreatedAt,
      @Param("cursorId") UUID cursorId,
      Pageable pageable
  );
}
