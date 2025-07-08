package com.orion.mdd.mddapi.services;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.security.KeyUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JWTService {

	@Value("${security.jwt.expiration-time}")
    private long expirationTime;
	
	private PrivateKey getPrivateKey() throws Exception {
		return KeyUtils.loadPrivateKey();
    }
	
	private PublicKey getPublicKey() throws Exception {
		return KeyUtils.loadPublicKey();
    }

	public String generateToken(String username) {
        try {
            Instant now = Instant.now();
            return Jwts.builder()
                    .claim("sub", username)
                    .claim("iat", Date.from(now))
                    .claim("exp", Date.from(now.plusMillis(expirationTime)))
                    .signWith(getPrivateKey(), io.jsonwebtoken.SignatureAlgorithm.RS256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

	public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractEmail(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
	
	public boolean isTokenValid(String token, User user) {
        String email = extractEmail(token);
        return email != null && email.equals(user.getEmail()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
	
	public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }
	
	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
	
	private Claims extractAllClaims(String token) {
        try {
			return Jwts.parser()
			        .verifyWith(getPublicKey())
			        .build()
			        .parseSignedClaims(token)
			        .getPayload();
		} catch (Exception e) {
			throw new RuntimeException("Error extracting claims from JWT token", e);
		}
    }
}