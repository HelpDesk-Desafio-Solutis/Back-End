package br.com.user.microservice.infra.config.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long jwtExpiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.validity}") long jwtExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpiration = jwtExpiration;
    }

    public boolean isTokenValid(String token) {
        try {
            if (token == null || token.isEmpty()) {
                return false;
            }

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String generateToken(String email, String role) {
        return generateToken(email, role, null);
    }

    public String generateToken(String email, String role, UUID uuid) {
        var builder = Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration));

        if (uuid != null) {
            builder.claim("uuid", uuid.toString());
        }

        return builder.signWith(secretKey).compact();
    }

    public String extractUsername(String token) {
        if (token == null || token.isEmpty()) {
            throw new JwtException("Token ausente");
        }

        return extractClaim(token, Claims::getSubject);
    }

    public UUID extractUuid(String token) {
        if (token == null || token.isEmpty()) {
            throw new JwtException("Token ausente");
        }

        String uuidString = extractClaim(token, claims -> claims.get("uuid", String.class));
        if (uuidString == null) {
            throw new JwtException("UUID não encontrado no token");
        }

        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            throw new JwtException("UUID inválido no token");
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
