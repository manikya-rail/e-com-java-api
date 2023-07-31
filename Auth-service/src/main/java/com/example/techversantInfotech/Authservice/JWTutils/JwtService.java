package com.example.techversantInfotech.Authservice.JWTutils;


import com.example.techversantInfotech.Authservice.Dto.UserDto;
import com.example.techversantInfotech.Authservice.UserRole;
import com.example.techversantInfotech.Authservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
 @Value("${jwt.secret}")
 private String SECRET_KEY;

    public String extractUsername(String token){
        return null;
    }

    public<T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    public Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public  boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            // The token is either invalid or has expired
            return false;
        }
    }

    public String generateToken(User user){
        Map<String,String> claims=Map.of("username",user.getUsername(),"role", String.valueOf(user.getRole()),"email",user.getEmail());
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(claims.get("email"))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();


    }

    private Key getSignInKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
