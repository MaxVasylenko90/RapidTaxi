package dev.mvasylenko.rapidtaxi.security.jwt;

import dev.mvasylenko.rapidtaxi.model.User;

public interface JwtService {
    /**
     * Generate new access token for user
     * @param user - user for whom the token will be generated
     * @return - new access token
     */
    String generateAccessToken(User user);

    /**
     * Generate new refresh token for user
     * @param user - user for whom the token will be generated
     * @return - new refresh token
     */
    String generateRefreshToken(User user);

    /**
     * Extract email from token claims
     * @param token - current token
     * @return - user email
     */
    String extractEmailFromTokenClaims(String token);

    /**
     * Token validation
     * @param token - token to validate
     * @return - true if valid, otherwise - false
     */
    boolean validateToken(String token);

    /**
     * Validate refresh token
     * @param refreshToken - refresh token to validate
     * @return - true if valid, otherwise - false
     */
    boolean isRefreshTokenValid(String refreshToken);

    /**
     * Delete refresh token
     * @param refreshToken - token to delete
     */
    void deleteRefreshToken(String refreshToken);
}
