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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service("authenticationServiceImpl")
public class AuthenticationServiceImpl implements AuthenticationService {
    public static final String MESSAGE = "message";
    private final Logger LOG = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        UserDetails userDetails = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User with current email wasn't found!"));

        return getResponseEntity(HttpStatus.OK, "accessToken", jwtService.generateAccessToken(userDetails));
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, String>> register(UserRegistrationDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            LOG.warn("Email already exists");
            return getResponseEntity(HttpStatus.CONFLICT, MESSAGE, "Current Email Already Used!");
        }

        try {
            userRepository.save(createUserInstance(userDto));
        } catch (Exception exception) {
            LOG.error("An exception occurred while saving user", exception);
            return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE,
                    "Something went wrong when tried to save user!");
        }
        LOG.info("User with email {} created successfully", userDto.getEmail());
        return getResponseEntity(HttpStatus.CREATED, MESSAGE, "User registered successfully!");
    }

    private User createUserInstance(UserRegistrationDto userDto) {
        User user = UserMapper.INSTANCE.userRegistrationDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.GUEST);
        return user;
    }

    private ResponseEntity<Map<String, String>> getResponseEntity(HttpStatus status, String key, String value) {
        return ResponseEntity.status(status).body(Collections.singletonMap(key, value));
    }
}
