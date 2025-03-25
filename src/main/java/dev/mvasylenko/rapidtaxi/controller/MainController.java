package dev.mvasylenko.rapidtaxi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

import static dev.mvasylenko.rapidtaxi.constants.Constants.MESSAGE;

@RestController
public class MainController {

    @GetMapping
    public Map<String, String> homePage() {
        return Collections.singletonMap(MESSAGE, "Welcome to RapidTaxi API!");
    }

    @GetMapping("/terms")
    public Map<String, String> termsAndConditions() {
        return Collections.singletonMap(MESSAGE, "This is Terms and Conditions page");
    }
}
