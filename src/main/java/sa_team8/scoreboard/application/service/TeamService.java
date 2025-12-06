package sa_team8.scoreboard.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.Manager;
import sa_team8.scoreboard.domain.entity.Team;
import sa_team8.scoreboard.domain.repository.CompetitionRepository;
import sa_team8.scoreboard.domain.repository.ManagerRepository;
import sa_team8.scoreboard.domain.repository.TeamRepository;
import sa_team8.scoreboard.global.exception.ApplicationException;
import sa_team8.scoreboard.global.exception.ErrorCode;
import sa_team8.scoreboard.global.security.SecurityUtil;
import sa_team8.scoreboard.presentation.competition.req.CreateTeamRequest;
import sa_team8.scoreboard.presentation.competition.req.UpdateTeamRequest;

@Service
@RequiredArgsConstructor
public class TeamService {

	private final TeamRepository teamRepository;
	private final CompetitionRepository competitionRepository;
	private final ManagerRepository managerRepository;

	@Transactional
	public void createTeam(UUID competitionId, CreateTeamRequest request) {
		Competition competition = getAndValidateCompetition(competitionId);
		Team team = Team.create(request.getName(), competition);
		competition.addTeam(team);
		teamRepository.save(team);
	}

	@Transactional
	public void updateTeam(UUID competitionId, UUID teamId, UpdateTeamRequest request) {
		Competition competition = getAndValidateCompetition(competitionId);
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new ApplicationException(ErrorCode.TEAM_NOT_FOUND));

		if (!team.getCompetition().equals(competition)) {
			throw new ApplicationException(ErrorCode.INVALID_INPUT_VALUE, "Team does not belong to the specified competition.");
		}

		team.update(request.getName());
		teamRepository.save(team);
	}

	@Transactional
	public void deleteTeam(UUID competitionId, UUID teamId) {
		Competition competition = getAndValidateCompetition(competitionId);
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new ApplicationException(ErrorCode.TEAM_NOT_FOUND));

		if (!team.getCompetition().equals(competition)) {
			throw new ApplicationException(ErrorCode.INVALID_INPUT_VALUE, "Team does not belong to the specified competition.");
		}

		competition.removeTeam(team);
		teamRepository.delete(team);
	}

	private Competition getAndValidateCompetition(UUID competitionId) {
		String managerEmail = SecurityUtil.getCurrentUsername();
		Manager manager = managerRepository.findByEmail(managerEmail)
			.orElseThrow(() -> new ApplicationException(ErrorCode.MANAGER_NOT_FOUND));

		Competition competition = competitionRepository.findById(competitionId)
			.orElseThrow(() -> new ApplicationException(ErrorCode.COMPETITION_NOT_FOUND));

		if (!manager.isManagedCompetition(competition)) {
			throw new ApplicationException(ErrorCode.COMPETITION_NOT_MANAGED);
		}
		return competition;
	}
}
