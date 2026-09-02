package br.com.gateway.microservice.filters;

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

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public boolean isTokenValid(String token) {
        try {
            if (token == null || token.isEmpty()) {
                return false;
            }

            Claims claims = extractAllClaims(token);

            System.out.println("JWT validado com sucesso");
            System.out.println("Subject: " + claims.getSubject());
            System.out.println("Role: " + claims.get("role"));
            System.out.println("UUID: " + claims.get("uuid"));
            System.out.println("Expiration: " + claims.getExpiration());

            return !claims.getExpiration().before(new Date());

        } catch (Exception e) {
            System.err.println("========== ERRO JWT ==========");
            System.err.println("Tipo: " + e.getClass().getName());
            System.err.println("Mensagem: " + e.getMessage());
            System.err.println("==============================");

            return false;
        }
    }

    public String extractUsername(String token) {
        if (token == null || token.isEmpty()) {
            throw new JwtException("Token ausente");
        }
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
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
