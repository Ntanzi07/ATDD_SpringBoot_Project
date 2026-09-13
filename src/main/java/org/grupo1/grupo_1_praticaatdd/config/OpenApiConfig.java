package org.grupo1.grupo_1_praticaatdd.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Grupo 1 - Prática ATDD API")
                        .version("1.0.0")
                        .description("API de gestão de aluno :D"));
    }
}
