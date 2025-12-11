package sa_team8.scoreboard.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import sa_team8.scoreboard.application.event.ScoreEventPublisher;
import sa_team8.scoreboard.domain.command.UpdateTeamCommand;
import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.Manager;
import sa_team8.scoreboard.domain.entity.ManagerCompetition;
import sa_team8.scoreboard.domain.entity.Team;
import sa_team8.scoreboard.domain.event.CompetitionDataChangeEvent;
import sa_team8.scoreboard.domain.repository.CompetitionRepository;
import sa_team8.scoreboard.domain.repository.ManagerCompetitionRepository;
import sa_team8.scoreboard.domain.repository.ManagerRepository;
import sa_team8.scoreboard.domain.repository.TeamRepository;
import sa_team8.scoreboard.global.exception.ApplicationException;
import sa_team8.scoreboard.global.exception.ErrorCode;
import sa_team8.scoreboard.global.security.SecurityUtil;
import sa_team8.scoreboard.presentation.competition.req.CompetitionActionMode;
import sa_team8.scoreboard.presentation.competition.req.CreateCompetitionRequest;
import sa_team8.scoreboard.presentation.competition.req.UpdateCompetitionRequest;
import sa_team8.scoreboard.presentation.competition.res.CreateCompetitionResponse;
import sa_team8.scoreboard.presentation.competition.res.GetCompetitionResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompetitionService {

  private final CompetitionRepository competitionRepository;
  private final ManagerRepository managerRepository;
  private final ManagerCompetitionRepository managerCompetitionRepository;
  private final TeamRepository teamRepository;
  private final ScoreBoardViewService scoreBoardViewService;

  public GetCompetitionResponse getCompetition(UUID competitionId) {
    String managerEmail = SecurityUtil.getCurrentUsername();

    // 관리자 엔티티 조회
    Manager manager = managerRepository.findByEmail(managerEmail)
        .orElseThrow(() -> new ApplicationException(ErrorCode.MANAGER_NOT_FOUND));

    Competition competition = competitionRepository.findById(competitionId)
        .orElseThrow(() -> new ApplicationException(ErrorCode.COMPETITION_NOT_FOUND));

    return GetCompetitionResponse.of(competition);
  }
  /**
   * UC-2.1, 2.6: 대회 생성 및 팀 등록
   * - 현재 로그인한 Manager 인증
   * - Competition 도메인 생성
   * - ScoreBoard & ScoreManageBoard 자동 생성 (도메인 로직 내부)
   * - Manager ↔ Competition 매핑 테이블 생성
   * - 팀 생성
   */
  @Transactional
  public CreateCompetitionResponse createCompetition(CreateCompetitionRequest request) {

    // 현재 로그인한 관리자 이메일 조회
    String managerEmail = SecurityUtil.getCurrentUsername();

    // 관리자 엔티티 조회
    Manager manager = managerRepository.findByEmail(managerEmail)
        .orElseThrow(() -> new ApplicationException(ErrorCode.MANAGER_NOT_FOUND));

    // Competition 도메인 생성 (정적 팩토리)
    Competition competition = Competition.create(
        request.getName(),
        request.getAnnouncement(),
        request.getDescription(),
        request.getStartTime(),
        request.getTotalTime(),
        request.getIsPublic(),
        request.getIsExternal()
    );

    // 대회 저장
    competitionRepository.save(competition);

    // 관리자 ↔ 대회 관계 생성
    ManagerCompetition managerCompetition = ManagerCompetition.createRelation(manager, competition);

    // 관계 저장
    managerCompetitionRepository.save(managerCompetition);

    // 팀 생성
    List<Team> list = request.getTeam().stream()
        .map(teamDto -> Team.create(teamDto.getName(), competition, teamDto.getInitialScore()))
        .toList();
    // 팀 배치 저장
    teamRepository.saveAll(list);

    // 프론트엔드용 publicId 반환
    return new CreateCompetitionResponse(
        competition.getId(),
        competition.getScoreBoard().getPublicId()
    );
  }

  /**
   * UC-2.2, 2.6: 대회 정보 수정, 팀 수정
   * - 관리 권한 확인 (Manager → Competition)
   * - Competition 의 ScoreBoard/MetaData 업데이트
   */
  @Transactional
  public void updateCompetition(UUID competitionId, UpdateCompetitionRequest request) {

    // 관리 권한 있는 Competition 조회
    Competition competition = getManagedCompetition(competitionId);

    // Patch 방식을 위해 도메인 메서드 내부에서 null ignore 처리
    competition.updateScoreBoard(
        request.getName(),
        request.getAnnouncement(),
        request.getDescription(),
        request.getStartTime(),
        request.getTotalTime()
    );
    // Dirty Checking 자동 반영

    // DTO → Command 변환
    List<UpdateTeamCommand> commands = request.getTeams().stream()
        .map(t -> new UpdateTeamCommand(
            t.getTeamId(),
            t.getName(),
            t.getInitialScore()
        ))
        .toList();

    competition.updateTeams(commands);
    
    ///competition 조회, history 조회 후 브로드 캐스트
    ScoreEventPublisher.publish(new CompetitionDataChangeEvent(
        scoreBoardViewService.getPublicScoreBoard(competition.getScoreBoard().getPublicId()),
        scoreBoardViewService.getHistory(competition.getScoreBoard().getPublicId())
    ));
  }


  /**
   * UC-2.4, UC-2.5: 대회 상태 변경
   * - WAITING → START, RUNNING → PAUSE, PAUSE → RESUME, RUNNING/PAUSE → CLOSE
   * - 모든 실제 로직은 CompetitionState 객체 안에서 처리
   * - Service 계층은 도메인 조작만 수행 (DDD 스타일)
   */
  @Transactional
  public void updateCompetitionAction(UUID competitionId, CompetitionActionMode actionMode) {

    // 관리 권한 검증된 Competition 조회
    Competition competition = getManagedCompetition(competitionId);

    // DTO enum → 도메인 내부 State 로 위임
    switch (actionMode) {
      case start -> competition.start();
      case pause  -> competition.pause();
      case resume -> competition.resume();
      case close  -> competition.close();
      default -> throw new ApplicationException(ErrorCode.INVALID_INPUT_VALUE);
    }

    // Dirty Checking
  }


  /**
   * Helper: 권한 검사 포함한 Competition 조회
   * - 현재 로그인한 관리자 이메일 기반
   * - Manager 가 관리 중인 Competition 인지 검증
   */
  private Competition getManagedCompetition(UUID competitionId) {

    // 현재 로그인 중인 관리자 조회 (Competitions 포함)
    String managerEmail = SecurityUtil.getCurrentUsername();

    Manager manager = managerRepository.findByEmailWithCompetitions(managerEmail)
        .orElseThrow(() -> new ApplicationException(ErrorCode.MANAGER_NOT_FOUND));

    // Competition 조회
    Competition competition = competitionRepository.findById(competitionId)
        .orElseThrow(() -> new ApplicationException(ErrorCode.COMPETITION_NOT_FOUND));

    // 해당 Competition 의 관리자인지 권한 검증
    if (!manager.isManagedCompetition(competition)) {
      throw new ApplicationException(ErrorCode.COMPETITION_NOT_MANAGED);
    }

    return competition;
  }
}
