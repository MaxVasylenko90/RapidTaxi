package dev.mvasylenko.rapidtaxi.controller;


import dev.mvasylenko.rapidtaxi.dto.UserDto;
import dev.mvasylenko.rapidtaxi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(@Qualifier("defaultUserService") UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my-account")
    public UserDto getUserPage(@RequestParam String email) {
        return userService.getUserPage(email);
    }
}
