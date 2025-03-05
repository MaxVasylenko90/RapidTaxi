package dev.mvasylenko.rapidtaxi.controller;

import dev.mvasylenko.rapidtaxi.dto.UserDto;
import dev.mvasylenko.rapidtaxi.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
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
        return userService.registerUser(userDto);
    }

    @GetMapping("/login")
    public Map<String, String> login() {
        return Collections.singletonMap("message", "This is login page");
    }

//    @PostMapping("/login")
//    public ResponseEntity<Map<String, String>> login(@RequestParam ("email") String email, @RequestParam ("password") String password) {
//
//
//    }

    @GetMapping("/terms")
    public Map<String, String> termsAndConditions() {
        return Collections.singletonMap("message", "This is Terms and Conditions page");
    }
}
