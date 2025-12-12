package sa_team8.scoreboard.application.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sa_team8.scoreboard.application.event.ScoreEventPublisher;
import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.Manager;
import sa_team8.scoreboard.domain.entity.ManagerCompetition;
import sa_team8.scoreboard.domain.entity.Score;
import sa_team8.scoreboard.domain.entity.ScoreManageBoard;
import sa_team8.scoreboard.domain.entity.Team;


import sa_team8.scoreboard.domain.event.ScoreUpdateEvent;
import sa_team8.scoreboard.domain.logic.history.ScoreEvent;
import sa_team8.scoreboard.domain.repository.CompetitionRepository;
import sa_team8.scoreboard.domain.repository.ManagerCompetitionRepository;
import sa_team8.scoreboard.domain.repository.ManagerRepository;
import sa_team8.scoreboard.domain.repository.ScoreManageBoardRepository;
import sa_team8.scoreboard.domain.repository.TeamRepository;
import sa_team8.scoreboard.global.exception.ApplicationException;
import sa_team8.scoreboard.global.exception.ErrorCode;
import sa_team8.scoreboard.global.security.SecurityUtil;
import sa_team8.scoreboard.presentation.score.req.ScoreChangeRequest;
import sa_team8.scoreboard.presentation.score.res.ScoreManageBoardListRes;
import sa_team8.scoreboard.presentation.score.res.ScoreManageBoardRes;
import sa_team8.scoreboard.presentation.score.res.ScoreManageBoardRes.TeamScoreRow;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScoreManageService {

  private final CompetitionRepository competitionRepo;
  private final ScoreManageBoardRepository scoreManageBoardRepository;
  private final ManagerCompetitionRepository managerCompetitionRepository;
  private final TeamRepository teamRepo;
  private final ManagerRepository managerRepo;
  private final ScoreBoardViewService scoreBoardViewService;

  @Transactional
  public ScoreManageBoardRes getScoreManageBoard(String manageBoardPublicId) {
    // 1. 매니저 로드 + 대회 로드 및 권한 검증
    String managerEmail = SecurityUtil.getCurrentUsername();
    Manager manager = managerRepo.findByEmailWithCompetitions(managerEmail)
        .orElseThrow(() -> new ApplicationException(ErrorCode.MANAGER_NOT_FOUND));
    
    // 스코어 매니지 보드 및 경기 조회
    ScoreManageBoard scoreManageBoard = scoreManageBoardRepository.findJoinCompetitionByPublicId(
            manageBoardPublicId)
        .orElseThrow(() -> new ApplicationException(ErrorCode.SCORE_MANAGE_BOARD_NOT_FOUND));
    Competition competition = scoreManageBoard.getCompetition();

    if (!manager.isManagedCompetition(competition)) {
      // 관리자 ↔ 대회 관계 생성
      ManagerCompetition managerCompetition = ManagerCompetition.createRelation(manager, competition);
      // 관계 저장
      managerCompetitionRepository.save(managerCompetition);
    }

    // 2. 대회에 속한 팀 로드 (Score까지 fetch join)
    List<Team> teams = teamRepo.findJoinScoreAllByCompetition(competition);

    // 3. DTO 변환
    List<TeamScoreRow> teamRows = teams.stream()
        .map(team -> new TeamScoreRow(
            team.getId(),
            team.getName(),
            team.getScore() != null ? team.getScore().getValue() : 0  // NPE 방지
        ))
        .toList();

    return new ScoreManageBoardRes(
        competition.getId(),
        competition.getMetaData().getName(),
        manager.getId(),
        manager.getName(),
        teamRows
    );
  }

  @Transactional
  public void changeScore(UUID competitionId, UUID teamId, ScoreChangeRequest req) {
    // 1. 매니저 로드 + 대회 로드 및 권한 검증
    String managerEmail = SecurityUtil.getCurrentUsername();
    Manager manager = managerRepo.findByEmailWithCompetitions(managerEmail)
        .orElseThrow(() -> new ApplicationException(ErrorCode.MANAGER_NOT_FOUND));

    Competition competition = competitionRepo.findById(competitionId)
        .orElseThrow(() -> new ApplicationException(ErrorCode.COMPETITION_NOT_FOUND));

    if (!manager.isManagedCompetition(competition)) {
      throw new ApplicationException(ErrorCode.COMPETITION_NOT_MANAGED);
    }

    // 2. 대상 팀 로드 및 대회 소속 검증
    Team targetTeam = teamRepo.findById(teamId)
        .orElseThrow(() -> new ApplicationException(ErrorCode.TEAM_NOT_FOUND));
    if (!targetTeam.getCompetition().equals(competition)) {
      throw new ApplicationException(ErrorCode.INVALID_INPUT_VALUE, "Target team does not belong to the specified competition.");
    }

    // 3. 상대 팀 로드 및 대회 소속 검증 (optional)
    Team againstTeam = null;
    if (req.teamIdAgainst() != null) {
      againstTeam = teamRepo.findById(req.teamIdAgainst())
          .orElseThrow(() -> new ApplicationException(ErrorCode.TEAM_NOT_FOUND));
      if (!againstTeam.getCompetition().equals(competition)) {
        throw new ApplicationException(ErrorCode.INVALID_INPUT_VALUE, "Against team does not belong to the specified competition.");
      }
    }

    // 4. Score 로드 및 점수 증감 정책 업데이트
    Score targetScore = targetTeam.getScore();

    // 5. 점수 변경
    // 1) Event 생성
    ScoreEvent event = req.eventType().createEvent(
        competition, targetTeam, againstTeam, manager, req.delta(), req.reason());

    // 2) Score 내부 값 변경
    targetScore.applyChange(req.delta(), req.policyType().createPolicy());

    // 3) 이벤트 발행
    ScoreEventPublisher.publish(ScoreUpdateEvent.create(competition.getTeams(),
        scoreBoardViewService.getHistory(competition.getScoreBoard().getPublicId())
        ));
  }

  @Transactional
  public List<ScoreManageBoardListRes> getManagedCompetitions() {
    // 1. 현재 로그인한 매니저 조회 (Competitions fetch join 포함)
    String managerEmail = SecurityUtil.getCurrentUsername();
    Manager manager = managerRepo.findByEmailWithCompetitions(managerEmail)
        .orElseThrow(() -> new ApplicationException(ErrorCode.MANAGER_NOT_FOUND));

    // 2. 매니저가 관리 중인 대회 정보 반환
    return manager.getManagerCompetitions().stream()
        .map(mc -> ScoreManageBoardListRes.of(mc.getCompetition()))
        .toList();
  }
}