package sa_team8.scoreboard.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

  @Value("${cors.url}")
  private String FRONT_URL;

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
            .allowedOriginPatterns(
                "http://localhost:5173",  // Vite/React
                "http://127.0.0.1:5173",
                "http://localhost:3000",  // CRA, Next.js
                "http://127.0.0.1:3000",
                FRONT_URL
            )
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
      }
    };
  }
}
