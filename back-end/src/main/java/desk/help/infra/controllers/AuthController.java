package desk.help.infra.controllers;

import desk.help.core.domains.UserDomain;
import desk.help.core.gateway.UserGateway;
import desk.help.infra.config.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserGateway userGateway;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request
    ) {

        Authentication auth =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()
                        )
                );

        UserDomain user = userGateway.findByEmail(request.email())
                .orElseThrow();

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getUuid()
        );

        Map<String, Object> response = new HashMap<>();

        response.put("token", token);
        response.put("type", "Bearer");
        response.put("uuid", user.getUuid());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(
            @RequestHeader(
                    value = "Authorization",
                    required = false
            ) String authHeader
    ) {

        String jwt = extractToken(authHeader);

        if (jwt == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "status", "unauthenticated",
                            "message", "Token não informado"
                    ));
        }

        try {

            if (!jwtService.isTokenValid(jwt)) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "status", "unauthenticated",
                                "message", "Token inválido ou expirado"
                        ));
            }

            String email = jwtService.extractUsername(jwt);

            String role = jwtService.extractClaim(
                    jwt,
                    claims -> claims.get("role", String.class)
            );

            return ResponseEntity.ok(
                    Map.of(
                            "status", "authenticated",
                            "email", email,
                            "role", role
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "status", "unauthenticated",
                            "message", "Token inválido"
                    ));
        }
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(
            @RequestHeader(
                    value = "Authorization",
                    required = false
            ) String authHeader
    ) {

        String jwt = extractToken(authHeader);

        if (jwt == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message", "Token não informado"
                    ));
        }

        try {

            if (!jwtService.isTokenValid(jwt)) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "message", "Token inválido ou expirado"
                        ));
            }

            String email = jwtService.extractUsername(jwt);

            UserDomain user = userGateway
                    .findByEmail(email)
                    .orElseThrow();

            Map<String, Object> response = new HashMap<>();

            response.put("uuid", user.getUuid());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
            response.put("active", user.getActive());

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message", "Não foi possível obter os dados do usuário"
                    ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "message", "Logout realizado com sucesso"
                )
        );
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            return null;
        }

        if (!authHeader.startsWith("Bearer ")) {
            return null;
        }

        return authHeader.substring(7);
    }

    public record LoginRequest(
            String email,
            String password
    ) {
    }
}