package com.bank.msauthentication.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Component
public class JwtUtil {


    @Value("${key_token}")
    private String privateKey;

    public String generateToken(String username) {
         return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setId(UUID.randomUUID().toString())
                .signWith(getSignKey())
                .compact();
    }



    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(privateKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(privateKey).build().parseClaimsJws(token).getBody();
    }

    public Claims decodeToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(privateKey.getBytes()));
    }
}
