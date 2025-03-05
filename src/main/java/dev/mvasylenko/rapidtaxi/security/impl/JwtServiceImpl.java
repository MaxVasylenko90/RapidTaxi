package dev.mvasylenko.rapidtaxi.security.impl;

import dev.mvasylenko.rapidtaxi.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {
    private static final int EXPIRATION_TIME = 3600000;
    private final String secretKey;

    public JwtServiceImpl(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return "";
    }

    @Override
    public String extractUsername(String token) {
        return "";
    }

    @Override
    public boolean validateToken(String token) {
        return false;
    }

    @Override
    public String extractToken(HttpServletRequest request) {
        return request.getHeader(secretKey);
    }
}
