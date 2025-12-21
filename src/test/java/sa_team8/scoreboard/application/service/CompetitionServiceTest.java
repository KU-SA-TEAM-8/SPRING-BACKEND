package sa_team8.scoreboard.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import sa_team8.scoreboard.domain.entity.Competition;
import sa_team8.scoreboard.domain.entity.Manager;
import sa_team8.scoreboard.domain.entity.ManagerCompetition;
import sa_team8.scoreboard.domain.entity.Team;
import sa_team8.scoreboard.domain.repository.CompetitionRepository;
import sa_team8.scoreboard.domain.repository.ManagerCompetitionRepository;
import sa_team8.scoreboard.domain.repository.ManagerRepository;
import sa_team8.scoreboard.domain.repository.TeamRepository;
import sa_team8.scoreboard.presentation.competition.req.CreateCompetitionRequest;
import sa_team8.scoreboard.presentation.competition.req.CreateTeamRequest;
import sa_team8.scoreboard.presentation.competition.res.CreateCompetitionResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompetitionService 단위 테스트")
class CompetitionServiceTest {

  @Mock
  ManagerRepository managerRepository;

  @Mock
  CompetitionRepository competitionRepository;

  @Mock
  ManagerCompetitionRepository managerCompetitionRepository;

  @Mock
  TeamRepository teamRepository;

  @InjectMocks
  CompetitionService competitionService;

  @Test
  @DisplayName("UT-14: 대회 생성 전체 흐름 검증")
  void createCompetitionFlow() {
    // ===============================
    // given: SecurityContext 설정
    // ===============================
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
            "admin@test.com", null, List.of()
        );

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);

    // ===============================
    // given: Manager mock
    // ===============================
    Manager manager = mock(Manager.class);
    when(managerRepository.findByEmail("admin@test.com"))
        .thenReturn(Optional.of(manager));

    // ===============================
    // given: 요청 DTO
    // ===============================
    CreateCompetitionRequest request = new CreateCompetitionRequest(
        "대회",
        "공지",
        "설명",
        Instant.now(),
        100,
        true,
        false,
        List.of(
            new CreateTeamRequest("팀1", 0),
            new CreateTeamRequest("팀2", 10)
        )
    );

    // ===============================
    // when
    // ===============================
    CreateCompetitionResponse response =
        competitionService.createCompetition(request);

    // ===============================
    // then: 흐름 검증
    // ===============================
    verify(managerRepository).findByEmail("admin@test.com");
    verify(competitionRepository).save(any(Competition.class));
    verify(managerCompetitionRepository)
        .save(any(ManagerCompetition.class));
    verify(teamRepository).saveAll(anyList());

    assertThat(response).isNotNull();
  }
}
