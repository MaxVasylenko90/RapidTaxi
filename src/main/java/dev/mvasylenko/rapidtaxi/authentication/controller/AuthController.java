package dev.mvasylenko.rapidtaxi.authentication.controller;

import dev.mvasylenko.rapidtaxi.authentication.service.AuthenticationService;
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
    private final AuthenticationService authenticationService;

    public AuthController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/registration")
    public Map<String, String> registration() {
        return Collections.singletonMap("message", "This is registration page");
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> registration(@RequestBody @Valid UserDto userDto) {
        authenticationService.register(userDto);
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


}
