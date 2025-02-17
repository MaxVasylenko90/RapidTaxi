package dev.mvasylenko.rapidtaxi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class MainController {

    @GetMapping
    public Map<String, String> homePage() {
        return Collections.singletonMap("message", "Welcome to RapidTaxi API!");
    }
}
