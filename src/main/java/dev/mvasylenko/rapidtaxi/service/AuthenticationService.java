package dev.mvasylenko.rapidtaxi.service;

import dev.mvasylenko.rapidtaxi.dto.UserLoginDto;
import dev.mvasylenko.rapidtaxi.dto.UserRegistrationDto;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthenticationService {
    /**
     * User registration
     * @param userDto - user with all initial data
     * @return - ResponseEntity with code and corresponding message
     */
    ResponseEntity<Map<String, String>> register(UserRegistrationDto userDto);

    /**
     * User authentication
     * @param request - (LoginRequestDto with credentials
     * @return - ResponseEntity with code and corresponding message
     */
    ResponseEntity<Map<String, String>> authenticate(UserLoginDto request);

    /**
     * Request ro refresh access token
     * @param refreshToken - old refresh token
     * @return - Map with new access and refresh tokens
     */
    ResponseEntity<Map<String, String>> refreshAccessToken(String refreshToken);
}
