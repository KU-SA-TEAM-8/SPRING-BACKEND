package sa_team8.scoreboard.global.security;

import lombok.RequiredArgsConstructor;
import sa_team8.scoreboard.global.security.jwt.JwtAuthenticationFilter;
import sa_team8.scoreboard.global.security.jwt.JwtTokenProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;

	private static final String[] PERMIT_ALL_PATTERNS = {
		"/auth/sign-up",
		"/auth/sign-in"
	};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity ) throws Exception {
		httpSecurity.httpBasic(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable);

		httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		httpSecurity.formLogin(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable);

		httpSecurity.authorizeHttpRequests(authorize ->
			authorize
				.requestMatchers(PERMIT_ALL_PATTERNS).permitAll()
				.requestMatchers(HttpMethod.DELETE, "/user").hasRole("ADMIN")
				.anyRequest().authenticated()
		);

		httpSecurity.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
			UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
