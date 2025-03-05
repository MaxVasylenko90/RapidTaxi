package dev.mvasylenko.rapidtaxi.service.impl;

import dev.mvasylenko.rapidtaxi.dto.UserDto;
import dev.mvasylenko.rapidtaxi.models.User;
import dev.mvasylenko.rapidtaxi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setEmail("test@gmail.com");
        userDto.setPassword("password");
        userDto.setName("testName");
        userDto.setPhoneNumber("123456789");
    }

    @Test
    void successfullyCreateUser() {
        //Arrange
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(Boolean.FALSE);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");

        //Act
        var response = sut.registerUser(userDto);

        //Assert
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(captor.capture());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully!", response.getBody().get("message"));
        assertEquals("testName", captor.getValue().getName());
        assertEquals("encodedPassword", captor.getValue().getPassword());
        assertEquals("test@gmail.com", captor.getValue().getEmail());
        assertEquals("123456789", captor.getValue().getPhoneNumber());
    }

    @Test
    void returnStatusConflictWhenEmailAlreadyExists() {
        //Arrange
        when(userRepository.existsByEmail(any())).thenReturn(Boolean.TRUE);

        //Act
        var response = sut.registerUser(userDto);

        //Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Current Email Already Used!", response.getBody().get("message"));
    }

    @Test
    void throwsExceptionWhenTryingToSaveUser() {
        //Arrange
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException());

        //Act
        var response = sut.registerUser(userDto);

        //Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Something went wrong when tried to save user!", response.getBody().get("message"));
    }
}
