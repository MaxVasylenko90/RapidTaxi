package dev.mvasylenko.rapidtaxi.controller;

import dev.mvasylenko.rapidtaxi.dto.UserDto;
import dev.mvasylenko.rapidtaxi.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
public class AuthController {
    private final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public Map<String, String> registration() {
        return Collections.singletonMap("message", "This is registration page");
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> registration(@RequestBody @Valid UserDto userDto) {
        userService.registerUser(userDto);
        return ResponseEntity
                    .status(HttpStatus.CREATED).
                    body(Collections.singletonMap("message", "User registered successfully!"));
    }

    @GetMapping("/terms")
    public Map<String, String> termsAndConditions() {
        return Collections.singletonMap("message", "This is Terms and Conditions page");
    }
}
