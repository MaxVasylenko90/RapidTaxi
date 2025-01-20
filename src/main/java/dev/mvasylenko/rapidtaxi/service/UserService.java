package dev.mvasylenko.rapidtaxi.service;

import dev.mvasylenko.rapidtaxi.entity.User;

public interface UserService {
    /**
     * Service for user registration
     * @param user - user model with initial info
     */
    void registerUser(User user);

    /**
     * Find user by username
     * @param username - name of the searching user
     * @return user model
     */
    User getUserByUsername(String username);
}
