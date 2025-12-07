package sa_team8.scoreboard.global.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI scoreboardOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Scoreboard API")
            .description("Open House Scoreboard 프로젝트 API 문서")
            .version("v1.0.0")
        );
  }
}
