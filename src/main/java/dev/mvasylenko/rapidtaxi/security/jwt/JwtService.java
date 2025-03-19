package dev.mvasylenko.rapidtaxi.security.jwt;

import jakarta.servlet.http.HttpServletRequest;

import java.time.Duration;

public interface JwtService {
    String generateAccessToken(String email);
    String generateRefreshToken(String email);
    String extractUsername(String token);
    boolean validateToken(String token);
    String extractToken(HttpServletRequest request);

    boolean isRefreshTokenValid(String refreshToken);

    void deleteRefreshToken(String refreshToken);

    Duration getRefreshTokenDuration(String token);
}
