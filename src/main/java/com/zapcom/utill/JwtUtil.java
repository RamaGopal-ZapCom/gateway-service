package com.zapcom.utill;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Created by Rama Gopal
 * Project Name - api-gateway-service
 */

@Component
public class JwtUtil {

/*
    @Value("${app.secret}")
    private  String secret;

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }*/



        @Value("${app.secret}")
        private String secret;

        private Key getSigningKey() {
            return Keys.hmacShaKeyFor(secret.getBytes());
        }

        public String generateToken(String email) {
            return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 *  24)) // 1 day expiry
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        }

        public Claims extractClaims(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }

        public String extractEmail(String token) {
            return extractClaims(token).getSubject();
        }

        public boolean isTokenValid(String token) {
            try {
                Claims claims = extractClaims(token);
                return claims.getExpiration().after(new Date());
            } catch (Exception e) {
                return false;
            }
        }


}

