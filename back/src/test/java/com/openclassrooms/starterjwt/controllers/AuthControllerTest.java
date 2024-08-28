package com.openclassrooms.starterjwt.controllers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private User mockedUser;
    private AuthController authController;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private LoginRequest loginRequest;
    @Mock
    SignupRequest signupRequest;
    @Mock
    private UserDetailsImpl userDetailsImpl;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
    }

    @Test
    void testAuthenticateUserValid() {
        String mockToken = "154612dfssqdqsdqsdqsdqsdqsdsqdf544ffze";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetailsImpl);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(mockToken);
        
        when(userDetailsImpl.getUsername()).thenReturn("user@test.com");
        when(userDetailsImpl.getId()).thenReturn(61L);
        when(userDetailsImpl.getFirstName()).thenReturn("firstname");
        when(userDetailsImpl.getLastName()).thenReturn("lastname");
        when(mockedUser.isAdmin()).thenReturn(true);

        when(userRepository.findByEmail(userDetailsImpl.getUsername())).thenReturn(Optional.of(mockedUser));
    
        ResponseEntity<?> res = authController.authenticateUser(loginRequest);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertTrue(res.getBody() instanceof JwtResponse);
        JwtResponse jwtResponse = (JwtResponse) res.getBody();
        assertEquals(mockToken, jwtResponse.getToken());
        assertEquals(61L, jwtResponse.getId());
        assertEquals("user@test.com", jwtResponse.getUsername());
        assertEquals("firstname", jwtResponse.getFirstName());
        assertEquals("lastname", jwtResponse.getLastName());
        assertTrue(jwtResponse.getAdmin());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateJwtToken(authentication);
        verify(userRepository).findByEmail("user@test.com");
    }
    @Test
    void testAuthenticateUserIsNull() {
        String mockToken = "154612dfssqdqsdqsdqsdqsdqsdsqdf544ffze";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetailsImpl);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(mockToken);
        
        when(userDetailsImpl.getUsername()).thenReturn("user@test.com");
        when(userDetailsImpl.getId()).thenReturn(61L);
        when(userDetailsImpl.getFirstName()).thenReturn("firstname");
        when(userDetailsImpl.getLastName()).thenReturn("lastname");

        when(userRepository.findByEmail(userDetailsImpl.getUsername())).thenReturn(Optional.empty());
    
        ResponseEntity<?> res = authController.authenticateUser(loginRequest);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertTrue(res.getBody() instanceof JwtResponse);
        JwtResponse jwtResponse = (JwtResponse) res.getBody();
        assertEquals(mockToken, jwtResponse.getToken());
        assertEquals(61L, jwtResponse.getId());
        assertEquals("user@test.com", jwtResponse.getUsername());
        assertEquals("firstname", jwtResponse.getFirstName());
        assertEquals("lastname", jwtResponse.getLastName());
        assertFalse(jwtResponse.getAdmin());
    }

    @Test
    void testRegisterUserValid() {
        when(signupRequest.getEmail()).thenReturn("test@test.com");
        when(signupRequest.getLastName()).thenReturn("lastname");
        when(signupRequest.getFirstName()).thenReturn("firstname");
        when(signupRequest.getPassword()).thenReturn("password");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        ResponseEntity<?> res = authController.registerUser(signupRequest);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        MessageResponse message = (MessageResponse) res.getBody();
        assertEquals("User registered successfully!", message.getMessage());

        verify(userRepository).existsByEmail("test@test.com");
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
    }
    @Test
    void testRegisterUserNotValid() {
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        ResponseEntity<?> res = authController.registerUser(signupRequest);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        MessageResponse message = (MessageResponse) res.getBody();
        assertEquals("Error: Email is already taken!", message.getMessage());
    }
}
