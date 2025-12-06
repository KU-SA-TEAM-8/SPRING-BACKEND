package sa_team8.scoreboard.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sa_team8.scoreboard.domain.entity.Manager;
import sa_team8.scoreboard.domain.repository.ManagerRepository;
import sa_team8.scoreboard.global.exception.ApplicationException;
import sa_team8.scoreboard.global.exception.ErrorCode;
import sa_team8.scoreboard.global.security.jwt.JwtToken;
import sa_team8.scoreboard.global.security.jwt.JwtTokenProvider;
import sa_team8.scoreboard.presentation.auth.req.RefreshRequest;
import sa_team8.scoreboard.presentation.auth.req.SignInRequest;
import sa_team8.scoreboard.presentation.auth.req.SignUpRequest;
import sa_team8.scoreboard.presentation.auth.res.SignInResponse;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        if (managerRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new ApplicationException(ErrorCode.USER_DUPLICATED);
        }

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        Manager manager = Manager.create(signUpRequest.getName(), signUpRequest.getEmail(), encodedPassword);
        managerRepository.save(manager);
    }

    @Transactional
    public SignInResponse signIn(SignInRequest signInRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return new SignInResponse(jwtToken);
    }

    @Transactional
    public void logout(String accessToken) {
        accessToken = accessToken.substring(7);
        String username = jwtTokenProvider.getUserNameFromToken(accessToken);
        jwtTokenProvider.deleteRefreshToken(username);
    }

    @Transactional
    public SignInResponse refresh(RefreshRequest refreshRequest) {
        if (!jwtTokenProvider.validateRefreshToken(refreshRequest.getRefreshToken())) {
            throw new ApplicationException(ErrorCode.INVALID_REFRESHTOKEN);
        }

        String username = jwtTokenProvider.getUserNameFromToken(refreshRequest.getRefreshToken());

        Authentication authentication = getAuthenticationForRefresh(username);

        JwtToken newTokens = jwtTokenProvider.generateToken(authentication);

        return new SignInResponse(newTokens);
    }

    private Authentication getAuthenticationForRefresh(String username) {
        UserDetails userDetails = managerRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new UsernamePasswordAuthenticationToken(
            userDetails,
            "",
            userDetails.getAuthorities()
        );
    }
}
