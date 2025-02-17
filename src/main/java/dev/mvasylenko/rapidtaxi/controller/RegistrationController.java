package dev.mvasylenko.rapidtaxi.controller;

import dev.mvasylenko.rapidtaxi.dto.UsetDto;
import dev.mvasylenko.rapidtaxi.mapper.UserMapper;
import dev.mvasylenko.rapidtaxi.service.UserService;
import dev.mvasylenko.rapidtaxi.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
public class RegistrationController {
    private final Logger LOG = LoggerFactory.getLogger(RegistrationController.class);

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public Map<String, String> registration() {
        return Collections.singletonMap("message", "This is registration page");
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> registration(@RequestBody UsetDto userDto) {
        try {
            userService.registerUser(UserMapper.INSTANCE.userDtoToUser(userDto));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "User wasn't registered!"));
        }
        return ResponseEntity
                .status(HttpStatus.CREATED).
                body(Collections.singletonMap("message", "User registered successfully!"));
    }

    @GetMapping("/terms")
    public Map<String, String> termsAndConditions() {
        return Collections.singletonMap("message", "This is Terms and Conditions page");
    }
}
