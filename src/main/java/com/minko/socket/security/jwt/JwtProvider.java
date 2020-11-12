package com.minko.socket.security.jwt;

import com.minko.socket.security.AccountPrincipal;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
@Slf4j
@Getter
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expired}")
    private long expiration;

    public String generateToken(Authentication authentication) {
        AccountPrincipal accountPrincipal = (AccountPrincipal) authentication.getPrincipal();
        return Jwts.builder().setSubject(accountPrincipal.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(expiration)))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String generateTokenByEmail(String email) {
        return Jwts.builder().setSubject(email)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(expiration)))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException
                | UnsupportedJwtException
                | ExpiredJwtException
                | IllegalArgumentException
                | SignatureException e) {
            log.error("error token validation");
            log.error(e.getMessage());
        }
        return false;
    }
}
