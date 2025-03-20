package dev.mvasylenko.rapidtaxi.controller;

import dev.mvasylenko.rapidtaxi.dto.UserRegistrationDto;
import dev.mvasylenko.rapidtaxi.security.jwt.JwtService;
import dev.mvasylenko.rapidtaxi.service.AuthenticationService;
import dev.mvasylenko.rapidtaxi.dto.UserLoginDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collections;
import java.util.Map;

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
        return Collections.singletonMap("message", "This is registration page");
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> registration(@RequestBody @Valid UserRegistrationDto userDto) {
        return authenticationService.register(userDto);
    }

    @GetMapping("/login")
    public Map<String, String> login() {
        return Collections.singletonMap("message", "This is login page");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        return authenticationService.authenticate(userLoginDto);
    }

    @PostMapping("/logout")
    public RedirectView logout(@RequestBody String refreshToken) {
        jwtService.deleteRefreshToken(refreshToken);
        return new RedirectView("/login");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody String refreshToken) {
        return authenticationService.refreshAccessToken(refreshToken);
    }
}
