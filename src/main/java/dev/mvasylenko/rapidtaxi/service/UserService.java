package dev.mvasylenko.rapidtaxi.service;

import dev.mvasylenko.rapidtaxi.models.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {
    /**
     * Service for user registration
     * @param user - user model with initial info
     */
    void registerUser(User user) throws Exception;

    /**
     * Find user by username
     * @param username - name of the searching user
     * @return user model
     */
    User getUserByUsername(String username);
}
