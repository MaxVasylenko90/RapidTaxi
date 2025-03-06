package dev.mvasylenko.rapidtaxi.authentication.service;

import dev.mvasylenko.rapidtaxi.dto.UserDto;

public interface AuthenticationService {
    /**
     * Service for registration
     * @param userDto -
     */
    void register(UserDto userDto);

    /**
     *
     * @param request
     * @return
     */
    String authenticate(LoginRequest request);
}
