    package com.demo.technicaltestbackend.utils;

    import io.jsonwebtoken.*;
    import io.jsonwebtoken.security.Keys;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Value;

    import javax.crypto.SecretKey;
    import java.util.Date;

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

        private String generateJWT(final String secret, final Long id, final Long expires){
            String token = Jwts.builder()
                    .issuedAt(new Date())
                    .expiration(TimeHelper.getExpireDate(expires))
                    .signWith(genereateSigningKey(secret))
                    .compact();
            log.trace("Token is added to the local cache for userID: {}, ttl: {}", id, expires);

            return token;
        }

        private Claims parseJWT(String token, String secret) throws JwtException {
            return Jwts.parser()
                    .verifyWith(genereateSigningKey(secret))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }

        public String generateAccessToken(final Long userId) {
            return generateJWT(accessTokenSecret, userId, accessTokenExpiresIn);
        }

        public String generateRefreshToken(final Long userId) {
            return generateJWT(refreshTokenSecret, userId, refreshTokenExpiresIn);
        }

    }
