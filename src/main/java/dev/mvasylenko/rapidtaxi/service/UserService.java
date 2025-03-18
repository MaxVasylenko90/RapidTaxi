package dev.mvasylenko.rapidtaxi.service;

import dev.mvasylenko.rapidtaxi.dto.UserDto;

public interface UserService {
    /**
     * Get user page
     *
     * @param email - user's email
     * @return Map with all user information
     */
    UserDto getUserPage(String email);
}
