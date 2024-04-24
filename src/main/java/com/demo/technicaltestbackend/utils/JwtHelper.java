package com.demo.technicaltestbackend.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.demo.technicaltestbackend.entities.AuthenticatedUser;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;

@Component
@Slf4j
public class JwtHelper {
    private final String accessTokenSecret;
    private final String refreshTokenSecret;

    @Getter
    private final Long accessTokenExpiresIn;

    @Getter
    private final Long refreshTokenExpiresIn;

    public JwtHelper(
            @Value("${access.token.secret}") String accessTokenSecret,
            @Value("${refresh.token.secret}") String refreshTokenSecret,
            @Value("${access.token.expires}") Long accessTokenExpiresIn,
            @Value("${refresh.token.expires}") Long refreshTokenExpiresIn) {
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
        this.accessTokenExpiresIn = accessTokenExpiresIn * 60 * 1000; // convert minutes to milisecond
        this.refreshTokenExpiresIn = refreshTokenExpiresIn * 60 * 60 * 1000; // convert hours to milisecond
    }

    private SecretKey genereateSigningKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private String generateJWT(final String secret, HashMap<String, Object> claims, final Long expires) {
        String token = Jwts.builder()
                .issuedAt(new Date())
                .claims().add(claims).and()
                .expiration(TimeHelper.getExpireDate(expires))
                .signWith(genereateSigningKey(secret))
                .compact();

        return token;
    }

    private HashMap<String, Object> genereateClaims(final Long userId, final String username) {
        HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("user_id", userId);
        claims.put("username", username);
        return claims;
    }

    private Claims parseJWT(String token, String secret) throws JwtException {
        return Jwts.parser()
                .verifyWith(genereateSigningKey(secret))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateAccessToken(final Long userId, final String username) {
        HashMap<String, Object> claims = genereateClaims(userId, username);
        return generateJWT(accessTokenSecret, claims, accessTokenExpiresIn);
    }

    public String generateRefreshToken(final Long userId, final String username) {
        HashMap<String, Object> claims = genereateClaims(userId, username);

        return generateJWT(refreshTokenSecret, claims, refreshTokenExpiresIn);
    }

    public HashMap<String, Object> getDataFromToken(String token, Boolean isAccessToken) {
        final String secret = isAccessToken ? accessTokenSecret : refreshTokenSecret;
        Claims claimsData = parseJWT(token, secret);

        HashMap<String, Object> expectedMap = new HashMap<>(claimsData);
        return expectedMap;
    }

    public Boolean validateToken(String token, Boolean isAccessToken, AuthenticatedUser userDetails) {
        final HashMap<String, Object> claimData = getDataFromToken(token, isAccessToken);
        log.info("Validated data: {}", claimData);
        final String username = claimData.get("username").toString();
        // final Date expiration = new Date(claimData.get("expiration").toString());

        return (username.equals(userDetails.getUsername()));
    }
}
