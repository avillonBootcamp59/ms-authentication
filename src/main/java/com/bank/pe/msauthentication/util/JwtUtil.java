package com.bank.pe.msauthentication.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Component
public class JwtUtil {


    @Value("${key_token}")
    private String privateKey;

    @Value("${jwt_token_duration_segundos}")
    private long tokenDuration;

    public String generateToken(String username) {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        SecretKeySpec key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenDuration))
                .setId(UUID.randomUUID().toString())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                log.error("Token expirado");
                return false;
            }
            log.info("Token válido para el usuario: " + claims.getSubject());
            return true;
        } catch (Exception e) {
            log.error("Error validando token: " + e.getMessage());
            return false;
        }
    }

    public Claims extractClaims(String token) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(privateKey);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);

            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Error extrayendo claims del token: " + e.getMessage());
            throw e;
        }
    }

    public Claims decodeToken(String token) {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSignKey() {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
