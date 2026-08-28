package br.com.gateway.microservice.filters;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Component
public class JwtAuthGatewayFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthGatewayFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest req,
            @NonNull HttpServletResponse res,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Ignora rotas públicas
        if (req.getServletPath().contains("/auth/login")) {
            filterChain.doFilter(req, res);
            return;
        }

        // Extrai o token do header ou cookie
        String jwt = extractJwtFromRequest(req);

        if (jwt == null) {
            filterChain.doFilter(req, res);
            return;
        }

        // Valida o token
        try {
            String userEmail = jwtService.extractUsername(jwt);
            String role = jwtService.extractRole(jwt);
            UUID userUuid = jwtService.extractUuid(jwt);

            if (userEmail != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                if (jwtService.isTokenValid(jwt)) {

                    String normalizedRole = role == null
                            ? ""
                            : role.trim().toUpperCase(Locale.ROOT);

                    var authorities = normalizedRole.isEmpty()
                            ? List.<SimpleGrantedAuthority>of()
                            : List.of(
                            new SimpleGrantedAuthority(
                                    "ROLE_" + normalizedRole
                            )
                    );

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userEmail,
                                    null,
                                    authorities
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(req)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authToken);

                    req.setAttribute("userUuid", userUuid);
                }
            }

        } catch (JwtException e) {
            logger.warn("Token JWT inválido: " + e.getMessage());
            res.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Token JWT inválido"
            );
            return;
        }

        filterChain.doFilter(req, res);
    }

    private String extractJwtFromRequest(HttpServletRequest req) {
        // Pegar token do header Authorization
        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        // Pegar token do cookie
        if (req.getCookies() != null) {
            for (var cookie : req.getCookies()) {
                if ("AUTH_TOKEN".equals(cookie.getName())) {
                    String encodedToken = cookie.getValue();
                    return decodeTokenFromCookie(encodedToken);
                }
            }
        }

        return null;
    }

    private String decodeTokenFromCookie(String encodedToken) {
        //Decodifica o token em base64 em caso de cookie
        if (encodedToken == null || encodedToken.isEmpty()) {
            return encodedToken;
        }

        try {
            return new String(Base64.getDecoder().decode(encodedToken), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            logger.warn("Falha ao decodificar o token do cookie: " + e.getMessage());
            return encodedToken;
        }
    }

}