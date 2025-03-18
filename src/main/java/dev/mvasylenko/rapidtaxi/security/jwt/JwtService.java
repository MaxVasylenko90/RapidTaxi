package dev.mvasylenko.rapidtaxi.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateAccessToken(UserDetails userDetails);
    String extractUsername(String token);
    boolean validateToken(String token);
    String extractToken(HttpServletRequest request);
}
