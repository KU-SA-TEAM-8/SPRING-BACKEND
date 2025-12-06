package sa_team8.scoreboard.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.Manager;
import sa_team8.scoreboard.domain.entity.ManagerCompetition;
import sa_team8.scoreboard.domain.repository.CompetitionRepository;
import sa_team8.scoreboard.domain.repository.ManagerCompetitionRepository;
import sa_team8.scoreboard.domain.repository.ManagerRepository;
import sa_team8.scoreboard.global.exception.ApplicationException;
import sa_team8.scoreboard.global.exception.ErrorCode;
import sa_team8.scoreboard.global.security.SecurityUtil;
import sa_team8.scoreboard.presentation.competition.req.CreateCompetitionRequest;
import sa_team8.scoreboard.presentation.competition.req.UpdateCompetitionRequest;

@Service
@RequiredArgsConstructor
public class CompetitionService {

	private final CompetitionRepository competitionRepository;
	private final ManagerRepository managerRepository;
	private final ManagerCompetitionRepository managerCompetitionRepository;

	public void createCompetition(CreateCompetitionRequest request) {
		String managerEmail = SecurityUtil.getCurrentUsername();
		Manager manager = managerRepository.findByEmail(managerEmail)
			.orElseThrow(() -> new ApplicationException(ErrorCode.MANAGER_NOT_FOUND));

		Competition competition = Competition.create(request.getName(), request.getAnnouncement(),
			request.getDescription(), request.getStartTime(), request.getTotalTime());

		competitionRepository.save(competition);

		ManagerCompetition managerCompetition = ManagerCompetition.builder()
			.manager(manager)
			.competition(competition)
			.build();
		managerCompetitionRepository.save(managerCompetition);
		manager.addManagerCompetitions(managerCompetition);
	}

	@Transactional
	public void updateCompetition(UUID competitionId, UpdateCompetitionRequest request) {
		Competition competition = getManagedCompetition(competitionId);
		competition.updateScoreBoard(request.getName(), request.getAnnouncement()
			, request.getDescription(), request.getStartTime(), request.getTotalTime());
		competitionRepository.save(competition);
	}

	@Transactional
	public void updateCompetitoinAction(UUID competitionId, String actionMode) {
		Competition competition = getManagedCompetition(competitionId);

		if(actionMode.equals("start")) {
			competition.start();
		}
		else if(actionMode.equals("stop")) {
			competition.close();
		}
		else {
			throw new ApplicationException(ErrorCode.INVALID_INPUT_VALUE);
		}
		competitionRepository.save(competition);
	}

	@Transactional
	public void restartCompetition(UUID competitionId, String mode) {
		Competition competition = getManagedCompetition(competitionId);

		if(mode.equals("resume")) {
			competition.resume();
		}
		else if(mode.equals("restart")) {
			competition.close();
			competition.start();
		}
		else {
			throw new ApplicationException(ErrorCode.INVALID_INPUT_VALUE);
		}
		competitionRepository.save(competition);
	}

	private Competition getManagedCompetition(UUID competitionId) {
		String managerEmail = SecurityUtil.getCurrentUsername();
		Manager manager = managerRepository.findByEmailWithCompetitions(managerEmail)
			.orElseThrow(() -> new ApplicationException(ErrorCode.MANAGER_NOT_FOUND));

		Competition competition = competitionRepository.findById(competitionId)
			.orElseThrow(() -> new ApplicationException(ErrorCode.COMPETITION_NOT_FOUND));

		if (!manager.isManagedCompetition(competition)) {
			throw new ApplicationException(ErrorCode.COMPETITION_NOT_MANAGED);
		}
		return competition;
	}
}
