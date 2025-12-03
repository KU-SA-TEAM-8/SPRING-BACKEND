package sa_team8.scoreboard.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.Team;
import sa_team8.scoreboard.domain.repository.CompetitionRepository;
import sa_team8.scoreboard.domain.repository.TeamRepository;
import sa_team8.scoreboard.global.exception.ApplicationException;
import sa_team8.scoreboard.global.exception.ErrorCode;
import sa_team8.scoreboard.presentation.competition.req.CreateTeamRequest;
import sa_team8.scoreboard.presentation.competition.req.UpdateTeamRequest;

@Service
@RequiredArgsConstructor
public class TeamService {

	private final TeamRepository teamRepository;
	private final CompetitionRepository competitionRepository;

	public void createTeam(UUID competitionId, CreateTeamRequest request) {
		Competition competition = competitionRepository.findById(competitionId)
			.orElseThrow(() -> new ApplicationException(ErrorCode.COMPETITION_NOT_FOUND));
		Team team = Team.create(request.getName(), competition);
		competition.addTeam(team);
		teamRepository.save(team);
		competitionRepository.save(competition);
	}

	public void updateTeam(UUID competitionId, UUID teamId, UpdateTeamRequest request) {
		Competition competition = competitionRepository.findById(competitionId)
			.orElseThrow(() -> new ApplicationException(ErrorCode.COMPETITION_NOT_FOUND));
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new ApplicationException(ErrorCode.TEAM_NOT_FOUND));
		team.update(request.getName());
		teamRepository.save(team);
		competitionRepository.save(competition);
	}

	public void deleteTeam(UUID competitionId, UUID teamId) {
		Competition competition = competitionRepository.findById(competitionId)
			.orElseThrow(() -> new ApplicationException(ErrorCode.COMPETITION_NOT_FOUND));
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new ApplicationException(ErrorCode.TEAM_NOT_FOUND));
		teamRepository.delete(team);
		competition.removeTeam(team);
		competitionRepository.save(competition);
	}
}
