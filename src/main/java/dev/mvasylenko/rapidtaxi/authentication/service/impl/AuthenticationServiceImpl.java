package dev.mvasylenko.rapidtaxi.authentication.service.impl;

import dev.mvasylenko.rapidtaxi.authentication.service.AuthenticationService;
import dev.mvasylenko.rapidtaxi.dto.UserDto;
import dev.mvasylenko.rapidtaxi.mapper.UserMapper;
import dev.mvasylenko.rapidtaxi.model.Role;
import dev.mvasylenko.rapidtaxi.model.User;
import dev.mvasylenko.rapidtaxi.repository.UserRepository;
import dev.mvasylenko.rapidtaxi.security.jwt.JwtService;
import dev.mvasylenko.rapidtaxi.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service("authenticationServiceImpl")
public class AuthenticationServiceImpl implements AuthenticationService {
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
    public void register(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            LOG.warn("Email already exists");
            return getResponseEntity(HttpStatus.CONFLICT,"Current Email Already Used!");
        }

        User user = UserMapper.INSTANCE.userDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.GUEST);

        try {
            userRepository.save(user);
            LOG.info("User with email {} created successfully", user.getEmail());
            return getResponseEntity(HttpStatus.CREATED, "User registered successfully!");
        } catch (Exception exception) {
            LOG.error("An exception occurred while saving user", exception);
            return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong when tried to save user!");
        }
    }

    public String authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return jwtService.generateToken(user);
    }

    private ResponseEntity<Map<String, String>> getResponseEntity(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Collections.singletonMap("message", message));
    }
}
