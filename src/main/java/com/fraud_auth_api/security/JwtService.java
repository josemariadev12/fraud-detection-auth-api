package com.fraud_auth_api.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fraud_auth_api.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

    @Value("${jwt.secret}") //local secret key
    private String secret; 
    private SecretKey secretKey;

    @PostConstruct 
    public void init(){
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(User user){
    String token = Jwts.builder()
    .subject(user.getEmail())
    .claim("role", user.getRole())
    .issuedAt(new Date())
    .expiration(new Date(System.currentTimeMillis()+86400000))
    .signWith(secretKey)
    .compact();
    return token;

    }
    private Claims decodeToken(String token){
        Claims claims = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();

        return claims;
    }

    public String extractEmail(String token){
        Claims claims = decodeToken(token);
        return claims.getSubject();
    }

    public String extractRole(String token){
        Claims claims = decodeToken(token);
        return claims.get("role", String.class);
    }


    public boolean validToken(UserDetails user,String token){
       final String email = extractEmail(token);
       return email.equals(user.getUsername());
    }

  

}
