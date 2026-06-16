package edu.cit.policios.campusbites.features.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() {
        User user = new User();
        user.setEmail("test@campus.edu");
        user.setPassword("password123");
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userRepository.findByEmail("test@campus.edu")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("$2a$hashed");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = authService.registerUser(user);

        assertNotNull(result);
        assertEquals("CUSTOMER", result.getRole());
        assertEquals("$2a$hashed", result.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void registerUser_DuplicateEmail_ThrowsException() {
        User existing = new User();
        existing.setEmail("exists@campus.edu");

        when(userRepository.findByEmail("exists@campus.edu")).thenReturn(Optional.of(existing));

        User newUser = new User();
        newUser.setEmail("exists@campus.edu");
        newUser.setPassword("pass");

        assertThrows(RuntimeException.class, () -> authService.registerUser(newUser));
    }

    @Test
    void login_ValidCredentials_ReturnsUser() {
        User stored = new User();
        stored.setEmail("user@campus.edu");
        stored.setPassword("$2a$hashed");

        when(userRepository.findByEmail("user@campus.edu")).thenReturn(Optional.of(stored));
        when(passwordEncoder.matches("plainpass", "$2a$hashed")).thenReturn(true);

        Optional<User> result = authService.login("user@campus.edu", "plainpass");

        assertTrue(result.isPresent());
    }

    @Test
    void login_InvalidPassword_ReturnsEmpty() {
        User stored = new User();
        stored.setEmail("user@campus.edu");
        stored.setPassword("$2a$hashed");

        when(userRepository.findByEmail("user@campus.edu")).thenReturn(Optional.of(stored));
        when(passwordEncoder.matches("wrongpass", "$2a$hashed")).thenReturn(false);

        Optional<User> result = authService.login("user@campus.edu", "wrongpass");

        assertFalse(result.isPresent());
    }

    @Test
    void login_NonexistentEmail_ReturnsEmpty() {
        when(userRepository.findByEmail("nobody@campus.edu")).thenReturn(Optional.empty());

        Optional<User> result = authService.login("nobody@campus.edu", "pass");

        assertFalse(result.isPresent());
    }
}