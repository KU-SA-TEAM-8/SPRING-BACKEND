package sa_team8.scoreboard.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.Manager;
import sa_team8.scoreboard.domain.entity.ManagerCompetition;
import sa_team8.scoreboard.domain.repository.CompetitionRepository;
import sa_team8.scoreboard.domain.repository.ManagerCompetitionRepository;
import sa_team8.scoreboard.domain.repository.ManagerRepository;
import sa_team8.scoreboard.global.exception.ApplicationException;
import sa_team8.scoreboard.global.exception.ErrorCode;
import sa_team8.scoreboard.presentation.competition.req.CreateCompetitionRequest;
import sa_team8.scoreboard.presentation.competition.req.UpdateCompetitionRequest;

@Service
@RequiredArgsConstructor
public class CompetitionService {

	private final CompetitionRepository competitionRepository;
	private final ManagerRepository managerRepository;
	private final ManagerCompetitionRepository managerCompetitionRepository;

	public void createCompetition(CreateCompetitionRequest request) {
		Competition competition = Competition.create(request.getName(), request.getAnnouncement(),
			request.getDescription(), request.getStartTime(), request.getTotalTime());

		//TODO Manager에 update
		// Manager manager = managerRepository.findById(managerId)
		// 	.orElseThrow(() -> new ApplicationException(ErrorCode.MANAGER_NOT_FOUND));
		// ManagerCompetition managerCompetition = ManagerCompetition.builder()
		// 	.manager(manager).competition(competition).build();
		// manager.addManagerCompetitions(managerCompetition);

		competitionRepository.save(competition);
		//managerCompetitionRepository.save(managerCompetition);
	}

	public void updateCompetition(UUID competitionId, UpdateCompetitionRequest request) {
		Competition competition = competitionRepository.findById(competitionId)
			.orElseThrow(() -> new ApplicationException(ErrorCode.COMPETITION_NOT_FOUND));
		competition.updateScoreBoard(request.getName(), request.getAnnouncement()
			,request.getDescription(), request.getStartTime(), request.getTotalTime());
		competitionRepository.save(competition);
	}

	public void updateCompetitoinAction(UUID competitionId, String actionMode) {
		Competition competition = competitionRepository.findById(competitionId)
			.orElseThrow(() -> new ApplicationException(ErrorCode.COMPETITION_NOT_FOUND));

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

	public void restartCompetition(UUID competitionId, String mode) {
		Competition competition = competitionRepository.findById(competitionId)
			.orElseThrow(() -> new ApplicationException(ErrorCode.COMPETITION_NOT_FOUND));

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
}
