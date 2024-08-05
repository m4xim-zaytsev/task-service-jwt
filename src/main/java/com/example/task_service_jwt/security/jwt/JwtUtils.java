package com.example.task_service_jwt.security.jwt;

import com.example.task_service_jwt.security.AppUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.tokenExpiration}")
    private Duration tokenExpiration;

    public String generateJwtToken(AppUserDetails userDetails){
        return generateTokenFromUserEmail(userDetails.getEmail());
    }

    public String generateTokenFromUserEmail(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + tokenExpiration.toMillis()))
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();
    }

    public String getUserEmail(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validate(String authToken){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        }catch (SignatureException e){
            log.error("Invalid signature:\s{}",e.getMessage());
        }catch (MalformedJwtException e){
            log.error("Invalid token:\s{}",e.getMessage());
        }catch (ExpiredJwtException e){
            log.error("Token is expired:\s{}",e.getMessage());
        }catch (UnsupportedJwtException e){
            log.error("Token is unsupported:\s{}",e.getMessage());
        }catch (IllegalArgumentException e){
            log.error("Claims string is empty:\s{}",e.getMessage());
        }
        return false;
    }
}
