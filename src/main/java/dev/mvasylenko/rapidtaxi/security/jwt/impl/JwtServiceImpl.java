package dev.mvasylenko.rapidtaxi.security.jwt.impl;

import dev.mvasylenko.rapidtaxi.model.User;
import dev.mvasylenko.rapidtaxi.security.jwt.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service("jwtServiceImpl")
public class JwtServiceImpl implements JwtService {
    private static final Logger LOG = LoggerFactory.getLogger(JwtServiceImpl.class);
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 5;
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 5;

    private final SecretKey secretKey;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public JwtServiceImpl(@Value("${jwt.secret.key}") String secretKey,
                          RedisTemplate<String, String> redisTemplate) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String generateAccessToken(User user) {
        return generateToken(user, ACCESS_TOKEN_EXPIRATION);
    }

    @Override
    public String generateRefreshToken(User user) {
        final var refreshToken = generateToken(user, REFRESH_TOKEN_EXPIRATION);
        redisTemplate.opsForValue().set(refreshToken, user.getEmail(), getRefreshTokenDuration(refreshToken));
        return refreshToken;
    }

    @Override
    public boolean isRefreshTokenValid(String refreshToken) {
        return redisTemplate.hasKey(refreshToken) ? validateToken(refreshToken) : Boolean.FALSE;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            return new Date().before(extractClaim(token, Claims::getExpiration));
        } catch (JwtException e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        try {
            redisTemplate.delete(refreshToken);
            LOG.info("Refresh token {} was deleted.", refreshToken);
        } catch (JwtException e) {
            LOG.error("JWT refresh token {} is not in the database and probably corrupted!", refreshToken);
        }
    }

    @Override
    public String extractEmailFromTokenClaims(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private String generateToken(User user, Long expiration) {
        return Jwts.builder()
                .setClaims(Map.of("role", user.getRole().name()))
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Duration getRefreshTokenDuration(String token) {
        return extractClaim(token, claims ->
                Duration.between(claims.getIssuedAt().toInstant(), claims.getExpiration().toInstant()));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody());
    }
}
