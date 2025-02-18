package dev.mvasylenko.rapidtaxi.service;

import dev.mvasylenko.rapidtaxi.dto.UserDto;
import dev.mvasylenko.rapidtaxi.models.User;

public interface UserService {
    /**
     * Service for user registration
     * @param userDto - userDto object with initial params
     */
    void registerUser(UserDto userDto);
}
