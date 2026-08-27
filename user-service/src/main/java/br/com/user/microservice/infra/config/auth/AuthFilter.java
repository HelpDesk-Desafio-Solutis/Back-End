package br.com.user.microservice.infra.config.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

public class AuthFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

    private final AuthService authService;

    private final TokenJwtManager tokenJwtManager;

    public AuthFilter(AuthService autenticacaoService, TokenJwtManager tokenJwtManager) {
        this.authService = autenticacaoService;
        this.tokenJwtManager = tokenJwtManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String username = null;
        String jwtToken = null;

        String reqTokenHeader = req.getHeader("Authorization");

        if (Objects.nonNull(reqTokenHeader) && reqTokenHeader.startsWith("Bearer ")) {
            jwtToken = reqTokenHeader.substring(7);

            try {
                username = tokenJwtManager.getUsernameFromToken(jwtToken);
            } catch (ExpiredJwtException exception) {
                LOGGER.info("[FALHA NA AUTENTICAÇÃO] - Token expirado, usuário: {} - {}", exception.getClaims().getSubject(), exception.getMessage());

                LOGGER.trace("[FALHA NA AUTENTICAÇÃO] - Stacktrace: %s", exception);

                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (JwtException exception) {
                LOGGER.info("[FALHA NA AUTENTICAÇÃO] - Token inválido, usuário: {}", exception.getMessage());

                LOGGER.trace("[FALHA NA AUTENTICAÇÃO] - Stacktrace: %s", exception);

                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            addUsernameInContext(req, username, jwtToken);
        }

        filterChain.doFilter(req, res);
    }

    private void addUsernameInContext(HttpServletRequest req, String username, String jwtToken) {
        UserDetails userDetails = authService.loadUserByUsername(username);

        if (tokenJwtManager.validateToken(jwtToken, userDetails)) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }

}
