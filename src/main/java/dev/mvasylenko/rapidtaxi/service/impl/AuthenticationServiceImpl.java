package dev.mvasylenko.rapidtaxi.service.impl;

import dev.mvasylenko.rapidtaxi.dto.UserRegistrationDto;
import dev.mvasylenko.rapidtaxi.service.AuthenticationService;
import dev.mvasylenko.rapidtaxi.dto.UserLoginDto;
import dev.mvasylenko.rapidtaxi.mapper.UserMapper;
import dev.mvasylenko.rapidtaxi.model.Role;
import dev.mvasylenko.rapidtaxi.model.User;
import dev.mvasylenko.rapidtaxi.repository.UserRepository;
import dev.mvasylenko.rapidtaxi.security.jwt.JwtService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

import static dev.mvasylenko.rapidtaxi.constants.Constants.MESSAGE;
import static dev.mvasylenko.rapidtaxi.constants.Constants.REFRESH_TOKEN;

@Service("authenticationServiceImpl")
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final String ACCESS_TOKEN = "accessToken";
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                     AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<Map<String, String>> authenticate(UserLoginDto loginRequest) {
        var email = loginRequest.getEmail();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword()));
        return generateNewTokens(email);
    }

    @Override
    public ResponseEntity<Map<String, String>> refreshAccessToken(String refreshToken) {
        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            jwtService.deleteRefreshToken(refreshToken);
            return getResponseEntity(HttpStatus.FORBIDDEN, MESSAGE, "Invalid refresh token. Please log in again.");
        }
        var email = jwtService.extractEmailFromTokenClaims(refreshToken);
        jwtService.deleteRefreshToken(refreshToken);
        return generateNewTokens(email);
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, String>> register(UserRegistrationDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            LOG.warn("Email already exists");
            return getResponseEntity(HttpStatus.CONFLICT, MESSAGE, "Current Email Already Used!");
        }

        try {
            userRepository.save(convertToUser(userDto));
        } catch (Exception exception) {
            LOG.error("An exception occurred while saving user", exception);
            return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE,
                    "Something went wrong when tried to save user!");
        }
        LOG.info("User with email {} created successfully", userDto.getEmail());
        return getResponseEntity(HttpStatus.CREATED, MESSAGE, "User registered successfully!");
    }

    private ResponseEntity<Map<String, String>> getResponseEntity(HttpStatus status, String key, String value) {
        return ResponseEntity.status(status).body(Collections.singletonMap(key, value));
    }

    private ResponseEntity<Map<String, String>> generateNewTokens(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with current email wasn't found!"));
        return ResponseEntity.ok()
                .body(Map.of(ACCESS_TOKEN, jwtService.generateAccessToken(user),
                        REFRESH_TOKEN, jwtService.generateRefreshToken(user)));
    }

    private User convertToUser(UserRegistrationDto userDto) {
        User user = UserMapper.INSTANCE.userRegistrationDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.GUEST);
        return user;
    }
}
