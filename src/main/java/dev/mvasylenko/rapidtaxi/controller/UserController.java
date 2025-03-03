package dev.mvasylenko.rapidtaxi.controller;


import dev.mvasylenko.rapidtaxi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(@Qualifier("defaultUserService") UserService userService) {
        this.userService = userService;
    }

}
