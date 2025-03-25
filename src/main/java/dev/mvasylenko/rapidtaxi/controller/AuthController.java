package dev.mvasylenko.rapidtaxi.controller;

import dev.mvasylenko.rapidtaxi.dto.UserRegistrationDto;
import dev.mvasylenko.rapidtaxi.security.jwt.JwtService;
import dev.mvasylenko.rapidtaxi.service.AuthenticationService;
import dev.mvasylenko.rapidtaxi.dto.UserLoginDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

import static dev.mvasylenko.rapidtaxi.constants.Constants.MESSAGE;
import static dev.mvasylenko.rapidtaxi.constants.Constants.REFRESH_TOKEN;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @Autowired
    public AuthController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @GetMapping("/registration")
    public Map<String, String> registration() {
        return Collections.singletonMap(MESSAGE, "This is registration page");
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> registration(@RequestBody @Valid UserRegistrationDto userDto) {
        return authenticationService.register(userDto);
    }

    @GetMapping("/login")
    public Map<String, String> login() {
        return Collections.singletonMap(MESSAGE, "This is login page");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        return authenticationService.authenticate(userLoginDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody Map<String, String> request) {
        jwtService.deleteRefreshToken(request.get(REFRESH_TOKEN));
        return ResponseEntity.ok(Collections.singletonMap(MESSAGE, "Logged out successfully"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody Map<String,String> request) {
        return authenticationService.refreshAccessToken(request.get(REFRESH_TOKEN));
    }
}
