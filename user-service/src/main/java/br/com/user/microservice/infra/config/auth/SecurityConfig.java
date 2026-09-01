package br.com.user.microservice.infra.config.auth;

import br.com.user.microservice.infra.config.auth.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                // CORS desabilitado: este serviço só deve ser acessado
                // internamente pelo Gateway, nunca diretamente pelo browser.
                // A checagem de Origin fica centralizada no Gateway.
                .cors(cors -> cors.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(authorize -> authorize

                        // Rotas públicas
                        .requestMatchers(
                                "/auth/login",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST, "/users"
                        ).permitAll()

                        // Admin
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        // Usuários autenticados
                        .requestMatchers("/tickets/**")
                        .hasAnyRole(
                                "CLIENT",
                                "ADMIN",
                                "TECHNICIAN"
                        )

                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                (request, response, authException) -> {
                                    response.setStatus(
                                            HttpStatus.UNAUTHORIZED.value()
                                    );

                                    response.getWriter().write(
                                            "{\"error\":\"Unauthorized\"}"
                                    );
                                }
                        )
                )

                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .deleteCookies("AUTH_TOKEN")
                        .clearAuthentication(true)
                        .permitAll()
                );

        http.addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

}