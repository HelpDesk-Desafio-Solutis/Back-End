package br.com.gateway.microservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI().
                        info(buildInfo())
                        .servers(buildServers())
                        .components(new Components()
                                .addSecuritySchemes("Bearer",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description("JWT Authentication Token")
                                )
                        )
                       .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("Bearer"));
        }

        private Info buildInfo() {
                return new Info()
                        .title("Desk Help API")
                        .version("1.0.0")
                        .description("API de documentação de HelpDesk")
                        .contact(new Contact()
                        .name("Equipe Desk Help")
                )
                .license(new License()
                        .name("UNLICENSED")
                );
        }

        private List<Server> buildServers() {
                return List.of(
                        new Server()
                                .url("http://localhost:8089")
                                .description("API Gateway"),
                        new Server()
                                .url("http://localhost:8081")
                                .description("User Service"),
                        new Server()
                                .url("http://localhost:8082")
                                .description("Ticket Service"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Notification Service")
                );
        }

}
