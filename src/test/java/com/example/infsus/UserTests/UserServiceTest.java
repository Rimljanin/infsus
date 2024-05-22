package com.example.infsus.UserTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.infsus.model.User;
import com.example.infsus.repository.UserRepository;
import com.example.infsus.requests.UserRequest;
import com.example.infsus.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwtToken;

    private UserRequest initialUserRequest;
    private UserRequest updatedUserRequest;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        initialUserRequest = new UserRequest();
        initialUserRequest.setName("Trpimir");
        initialUserRequest.setLastName("Trpimirović");
        initialUserRequest.setUserName("Kralj");

        updatedUserRequest = new UserRequest();
        updatedUserRequest.setName("Pero");
        updatedUserRequest.setLastName("Perić");
        updatedUserRequest.setUserName("Rope");

        user = new User(initialUserRequest);
        user.setEmail("trpi@example.com");

        when(jwtToken.getClaim("email")).thenReturn("trpi@example.com");
        when(authentication.getPrincipal()).thenReturn(jwtToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(initialUserRequest);
        assertNotNull(result);
        assertEquals("Trpimir", result.getName());
        assertEquals("Trpimirović", result.getLastName());
        assertEquals("Kralj", result.getUserName());
        assertEquals("trpi@example.com", result.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(user);

        User result = userService.updateUser(updatedUserRequest);
        assertNotNull(result);
        assertEquals("Pero", result.getName());
        assertEquals("Perić", result.getLastName());
        assertEquals("Rope", result.getUserName());
        assertEquals("trpi@example.com", result.getEmail());

        verify(userRepository, times(1)).findByEmail(any(String.class));
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(updatedUserRequest);
        });

        assertEquals("Klijent nije pronađen", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(any(String.class));
    }

    @Test
    public void testUpdateUser_UnauthorizedAccess() {
        SecurityContextHolder.getContext().setAuthentication(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(updatedUserRequest);
        });

        assertEquals("Nedozvoljen pristup", exception.getMessage());
    }
}

