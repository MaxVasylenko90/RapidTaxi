package dev.mvasylenko.rapidtaxi.service.impl;

import dev.mvasylenko.rapidtaxi.dto.UserDto;
import dev.mvasylenko.rapidtaxi.mapper.UserMapper;
import dev.mvasylenko.rapidtaxi.models.User;
import dev.mvasylenko.rapidtaxi.repository.UserRepository;
import dev.mvasylenko.rapidtaxi.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service("defaultUserService")
public class UserServiceImpl implements UserService {
    private final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity<Map<String, String>> registerUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            return getResponseEntity(HttpStatus.CONFLICT,"Current Email Already Used!");
        }
        User user = UserMapper.INSTANCE.userDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("GUEST");
        try {
            userRepository.save(user);
            return getResponseEntity(HttpStatus.CREATED, "User registered successfully!");
        } catch (Exception exception) {
            LOG.error("An exception occurred while saving user", exception);
            return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong when tried to save user!");
        }
    }

    private ResponseEntity<Map<String, String>> getResponseEntity(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Collections.singletonMap("message", message));
    }
}
