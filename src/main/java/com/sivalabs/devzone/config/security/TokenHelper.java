package com.sivalabs.devzone.config.security;

import com.sivalabs.devzone.ApplicationProperties;
import com.sivalabs.devzone.domain.exceptions.DevZoneException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenHelper {
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static final String AUDIENCE_WEB = "web";

    private final ApplicationProperties applicationProperties;

    public String getUsernameFromToken(String token) {
        String username = null;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return username;
    }

    public String refreshToken(String token) {
        String refreshedToken = null;
        Date a = new Date();
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            claims.setIssuedAt(a);
            refreshedToken =
                    Jwts.builder()
                            .setClaims(claims)
                            .setExpiration(generateExpirationDate())
                            .signWith(
                                    SIGNATURE_ALGORITHM, applicationProperties.getJwt().getSecret())
                            .compact();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return refreshedToken;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setIssuer(applicationProperties.getJwt().getIssuer())
                .setSubject(username)
                .setAudience(AUDIENCE_WEB)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, applicationProperties.getJwt().getSecret())
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims =
                    Jwts.parser()
                            .setSigningKey(applicationProperties.getJwt().getSecret())
                            .parseClaimsJws(token)
                            .getBody();
        } catch (Exception e) {
            throw new DevZoneException(e);
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(
                System.currentTimeMillis() + applicationProperties.getJwt().getExpiresIn() * 1000);
    }

    public long getExpiredIn() {
        return applicationProperties.getJwt().getExpiresIn();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username != null && username.equals(userDetails.getUsername());
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(applicationProperties.getJwt().getHeader());
    }
}
