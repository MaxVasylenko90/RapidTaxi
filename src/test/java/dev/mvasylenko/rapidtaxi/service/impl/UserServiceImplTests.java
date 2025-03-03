package dev.mvasylenko.rapidtaxi.service.impl;

import dev.mvasylenko.rapidtaxi.dto.UserDto;
import dev.mvasylenko.rapidtaxi.mapper.UserMapper;
import dev.mvasylenko.rapidtaxi.models.User;
import dev.mvasylenko.rapidtaxi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl sut;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setEmail("test@gmail.com");
        userDto.setPassword("password");
        userDto.setName("testName");

        user = UserMapper.INSTANCE.userDtoToUser(userDto);

    }

    @Test
    void successfullyCreateUser() {
        when(userRepository.existsByEmail(any())).thenReturn(Boolean.FALSE);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");

        var response = sut.registerUser(userDto);

        assertEquals("encodedPassword", user.getPassword());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully!", response.getBody().get("message"));
        verify(userRepository.save(any(User.class)), times(1));
    }
}
