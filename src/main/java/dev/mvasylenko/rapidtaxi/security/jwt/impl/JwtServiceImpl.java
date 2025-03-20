package dev.mvasylenko.rapidtaxi.security.jwt.impl;

import dev.mvasylenko.rapidtaxi.security.jwt.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

@Service("jwtServiceImpl")
public class JwtServiceImpl implements JwtService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtServiceImpl.class);
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 1;
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    private final SecretKey secretKey;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtServiceImpl(@Value("${jwt.secret.key}") String secretKey,
                          RedisTemplate<String, String> redisTemplate) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String generateAccessToken(String email) {
        return generateToken(email, ACCESS_TOKEN_EXPIRATION);
    }

    @Override
    public String generateRefreshToken(String email) {
        final var refreshToken = generateToken(email, REFRESH_TOKEN_EXPIRATION);
        redisTemplate.opsForValue().set(refreshToken, email, getRefreshTokenDuration(refreshToken));
        return refreshToken;
    }

    @Override
    public String extractToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .filter(header -> header.startsWith(BEARER_PREFIX))
                .map(header -> header.substring(BEARER_PREFIX.length()))
                .orElse(null);
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
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isRefreshTokenValid(String refreshToken) {
        return redisTemplate.hasKey(refreshToken) ? validateToken(refreshToken) : Boolean.FALSE;
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.delete(refreshToken);
        LOGGER.info("Refresh token {} was deleted.", refreshToken);
    }

    private String generateToken(String email, Long expiraion) {
        return Jwts.builder()
                .setClaims(new HashMap())
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiraion))
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
