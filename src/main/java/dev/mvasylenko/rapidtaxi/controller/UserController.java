package dev.mvasylenko.rapidtaxi.controller;

import dev.mvasylenko.rapidtaxi.dto.UserDto;
import dev.mvasylenko.rapidtaxi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(@Qualifier("defaultUserService") UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my-account")
    public UserDto getUserPage(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getUserPage(userDetails.getUsername());
    }
}
