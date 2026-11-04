package com.bankapp.service;

import com.bankapp.model.dto.AuthResponse;
import com.bankapp.model.dto.LoginRequest;
import com.bankapp.model.dto.RegisterRequest;
import com.bankapp.model.entity.User;
import com.bankapp.repository.UserRepository;
import com.bankapp.util.JwtProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_ShouldReturnAuthResponse_WhenCredentialsAreValid() {
        // Arrange
        String email = "test@example.com";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";
        String token = "mock-jwt-token";
        long expirationMs = 3600000L;

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(rawPassword);

        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword(encodedPassword);
        mockUser.setRole("USER");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtProvider.generateToken(mockUser)).thenReturn(token);
        when(jwtProvider.getExpirationMs()).thenReturn(expirationMs);

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(token, response.getToken());
        assertEquals(email, response.getEmail());
        assertEquals("USER", response.getRole());
        assertEquals(expirationMs, response.getExpiresIn());

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
        verify(jwtProvider, times(1)).generateToken(mockUser);
    }

    @Test
    void login_ShouldThrowException_WhenEmailNotFound() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("notfound@example.com");
        loginRequest.setPassword("password123");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtProvider, never()).generateToken(any());
    }

    @Test
    void login_ShouldThrowException_WhenPasswordDoesNotMatch() {
        // Arrange
        String email = "test@example.com";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword("wrongPassword");

        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword("encodedPassword123");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrongPassword", mockUser.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches("wrongPassword", mockUser.getPassword());
        verify(jwtProvider, never()).generateToken(any());
    }

    @Test
    void register_ShouldSaveUser_WhenEmailNotExists() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@example.com");
        request.setPassword("password123");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        authService.register(request);

        // Assert
        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@example.com");
        request.setPassword("password123");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(request)
        );

        assertEquals("Email already registered", exception.getMessage());
        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
}