package dev.mvasylenko.rapidtaxi.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(UserDetails userDetails);
    String extractUsername(String token);
    boolean validateToken(String token);
    String extractToken(HttpServletRequest request);

}
