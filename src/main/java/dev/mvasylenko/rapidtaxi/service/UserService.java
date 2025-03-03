package dev.mvasylenko.rapidtaxi.service;

import dev.mvasylenko.rapidtaxi.dto.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {
    /**
     * Service for user registration
     * @param userDto - userDto object with initial params
     * @return -ResponseEntity with corresponded code
     */
    ResponseEntity<Map<String, String>>registerUser(UserDto userDto);
}
