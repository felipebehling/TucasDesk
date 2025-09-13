package com.example.Tucasdesk.security;

import com.example.Tucasdesk.model.Usuario;
import com.example.Tucasdesk.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * Service for handling JWT operations, such as token generation and validation.
 */
@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Creates a signing key from the configured JWT secret.
     *
     * @return A {@link Key} object for signing and verifying JWTs.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * Generates a JWT for the given user.
     *
     * @param usuario The user for whom the token will be generated.
     * @return A JWT as a string.
     */
    public String generateToken(Usuario usuario) {
        return Jwts.builder()
                .setIssuer("TucasDesk API")
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extracts the subject (username) from the given JWT.
     *
     * @param token The JWT from which to extract the subject.
     * @return The subject of the token.
     */
    public String getSubjectFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
}
