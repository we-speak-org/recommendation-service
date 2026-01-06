package org.wespeak.recommendation.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI recommendationOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("WeSpeak Recommendation Service API")
                .description("API pour recommandations personnalisées (lessons & conversations)")
                .version("1.0.0"))
        .externalDocs(
            new ExternalDocumentation()
                .description("WeSpeak Specifications")
                .url("https://github.com/we-speak-org/wespeak-specifications"));
  }
}
