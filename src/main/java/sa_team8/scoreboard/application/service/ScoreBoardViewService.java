package sa_team8.scoreboard.application.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.CompetitionMetaData;
import sa_team8.scoreboard.domain.entity.ScoreBoard;
import sa_team8.scoreboard.domain.entity.ScoreHistory;
import sa_team8.scoreboard.domain.entity.Team;
import sa_team8.scoreboard.domain.repository.CompetitionRepository;
import sa_team8.scoreboard.domain.repository.ScoreHistoryRepository;
import sa_team8.scoreboard.domain.repository.TeamRepository;
import sa_team8.scoreboard.global.exception.ApplicationException;
import sa_team8.scoreboard.global.exception.ErrorCode;
import sa_team8.scoreboard.presentation.score.res.ScoreBoardListRes;
import sa_team8.scoreboard.presentation.score.res.ScoreBoardListRes.ScoreBoardRow;
import sa_team8.scoreboard.presentation.score.res.ScoreBoardRes;
import sa_team8.scoreboard.presentation.score.res.ScoreBoardRes.ScoreBoardTeamRow;
import sa_team8.scoreboard.presentation.score.res.ScoreHistoryRes;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScoreBoardViewService {

  private final ScoreBoardRepository scoreBoardRepo;
  private final ScoreHistoryRepository scoreHistoryRepo;
  private final TeamRepository teamRepo;

  public ScoreBoardRes getPublicScoreBoard(String publicId) {
    ScoreBoard scoreBoard = scoreBoardRepo.findJoinCompetitionByPublicId(publicId)
        .orElseThrow(() -> new ApplicationException(ErrorCode.SCORE_BOARD_NOT_FOUND));
    Competition comp = scoreBoard.getCompetition();
    CompetitionMetaData metaData = comp.getMetaData();

    List<Team> teams = teamRepo.findJoinScoreAllByCompetitionOrderByScore_ValueDesc(comp);

    List<ScoreBoardTeamRow> rows = teams.stream()
        .map(team -> new ScoreBoardTeamRow(
            team.getId(),
            team.getName(),
            team.getScore().getValue()
        ))
        .toList();

    return new ScoreBoardRes(
        metaData.getName(),
        metaData.getAnnouncement(),
        metaData.getDescription(),
        metaData.getStartTime(),
        metaData.getTotalTime(),
        rows
    );
  }

  public List<ScoreHistoryRes> getHistory(String publicId) {
    ScoreBoard scoreBoard = scoreBoardRepo.findJoinCompetitionByPublicId(publicId)
        .orElseThrow(() -> new ApplicationException(ErrorCode.SCORE_BOARD_NOT_FOUND));
    Competition comp = scoreBoard.getCompetition();

    List<ScoreHistory> histories = scoreHistoryRepo.findByCompetitionOrderByCreatedAtDesc(comp);

    return histories.stream()
        .map(h -> new ScoreHistoryRes(
            h.getTargetTeam().getName(),
            h.getAgainstTeam().getName(),
            h.getDelta(),
            h.getReason(),
            h.getCreatedAtToInstant()
        ))
        .toList();
  }

  public ScoreBoardListRes listPublicScoreboards(LocalDateTime cursorCreatedAt, UUID cursorId, int size) {
    Pageable pageable = PageRequest.of(0, size);

    List<ScoreBoard> result = scoreBoardRepo.findNextPage(
        cursorCreatedAt,
        cursorId,
        pageable
    );

    // 다음 커서 계산
    LocalDateTime nextCursorCreatedAt = null;
    UUID nextCursorId = null;

    if (!result.isEmpty()) {
      ScoreBoard last = result.get(result.size() - 1);
      nextCursorCreatedAt = last.getCreatedAt();
      nextCursorId = last.getId();
    }

    List<ScoreBoardRow> list = result.stream()
        .map(ScoreBoardRow::from)
        .toList();

    return new ScoreBoardListRes(list, nextCursorCreatedAt, nextCursorId);
  }
}
