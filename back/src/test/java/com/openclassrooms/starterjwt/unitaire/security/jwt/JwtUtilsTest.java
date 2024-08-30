package com.openclassrooms.starterjwt.unitaire.security.jwt;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    @Mock
    private UserDetailsImpl userDetailsImpl;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtUtils jwtUtils;

    private String jwtSecret = "16541515qs4dqsdq"; // The secret key
    private int jwtExpirationMs = 86400000; // 1 day in milliseconds

    @BeforeEach
    void setUp() {
        // Inject the secret and expiration values into the JwtUtils instance
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
    }

    @Test
    @Tag("Security")
    @DisplayName("Test generating a JWT token should return a valid token")
    void testGenerateJwtToken() {
        // Arrange: Set up mock responses
        String mockUsername = "username";
        when(authentication.getPrincipal()).thenReturn(userDetailsImpl);
        when(userDetailsImpl.getUsername()).thenReturn(mockUsername);

        // Act: Generate the JWT token
        String resToken = jwtUtils.generateJwtToken(authentication);

        // Assert: Verify the generated token and its content
        assertNotNull(resToken);

        String parsedResToken = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(resToken)
            .getBody()
            .getSubject();
        
        assertEquals(mockUsername, parsedResToken);

        verify(authentication).getPrincipal();
        verify(userDetailsImpl).getUsername();
    }

    @Test
    @Tag("Security")
    @DisplayName("Test extracting username from JWT token should return the correct username")
    void testGetUserNameFromJwtToken() {
        // Arrange: Set up mock responses
        String mockUsername = "username";
        when(authentication.getPrincipal()).thenReturn(userDetailsImpl);
        when(userDetailsImpl.getUsername()).thenReturn(mockUsername);

        // Act: Generate the JWT token and extract the username
        String resToken = jwtUtils.generateJwtToken(authentication);
        String res = jwtUtils.getUserNameFromJwtToken(resToken);

        // Assert: Verify the extracted username
        assertEquals(mockUsername, res);

        verify(authentication).getPrincipal();
        verify(userDetailsImpl).getUsername();
    }

    @Test
    @Tag("Security")
    @DisplayName("Test validating a valid JWT token should return true")
    void testValidateJwtToken() {
        // Arrange: Create a valid JWT token
        String validToken = Jwts.builder()
            .setSubject("username")
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();

        // Act & Assert: Validate the token and verify it is valid
        assertTrue(jwtUtils.validateJwtToken(validToken));
    }

    @Test
    @Tag("Security")
    @DisplayName("Test validating a JWT token with an incorrect signature should return false")
    void testValidateJwtTokenErrorSignature() {
        // Arrange: Use an incorrect secret to simulate a signature error
        String notValidToken = Jwts.builder()
            .setSubject("username")
            .signWith(SignatureAlgorithm.HS512, "incorrectSecret")
            .compact();
    
        // Act & Assert: Validate the token and verify it is not valid
        assertFalse(jwtUtils.validateJwtToken(notValidToken));
    }

    @Test
    @Tag("Security")
    @DisplayName("Test validating a malformed JWT token should return false")
    void testValidateJwtTokenErrorMalformed() {
        // Arrange: Create a malformed JWT token
        String notValid = "this.is.a.fake.jwt.token";
    
        // Act & Assert: Validate the token and verify it is not valid
        assertFalse(jwtUtils.validateJwtToken(notValid));
    }

    @Test
    @Tag("Security")
    @DisplayName("Test validating an expired JWT token should return false")
    void testValidateJwtTokenErrorExpired() {
        // Arrange: Create an expired JWT token
        String expiredToken = Jwts.builder()
            .setSubject("username")
            .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Set the token as expired
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();

        // Act & Assert: Validate the token and verify it is not valid
        assertFalse(jwtUtils.validateJwtToken(expiredToken));
    }

    @Test
    @Tag("Security")
    @DisplayName("Test validating an empty JWT token should return false")
    void testValidateJwtTokenErrorEmpty() {
        // Act & Assert: Validate an empty token and verify it is not valid
        assertFalse(jwtUtils.validateJwtToken(""));
    }
}