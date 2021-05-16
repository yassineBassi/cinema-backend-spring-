package com.yassinebassii.cinema.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    String SECRET = "sqfhkljqhflqSKFUIEJFKLQHFJKLQSGJSQKFHSQJKF";
    Date EXPIRATION_DATE = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10);

    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    public String generateToken(UserDetails userDetails){
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("email", userDetails.getUsername());
        claims.put("role", userDetails.getAuthorities());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String username = extractUserName(token);
        return username.equals(userDetails.getUsername()) && isTokenExpired(token);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token)
                .before(new Date());
    }
}
