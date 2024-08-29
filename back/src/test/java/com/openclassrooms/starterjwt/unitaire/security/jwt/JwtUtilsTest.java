package com.openclassrooms.starterjwt.unitaire.security.jwt;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
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
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
    }
    @Test
    void testGenerateJwtToken() {
        String mockUsername = "username";
        when(authentication.getPrincipal()).thenReturn(userDetailsImpl);
        when(userDetailsImpl.getUsername()).thenReturn(mockUsername);

        String resToken = jwtUtils.generateJwtToken(authentication);

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
    void testGetUserNameFromJwtToken() {
        String mockUsername = "username";
        when(authentication.getPrincipal()).thenReturn(userDetailsImpl);
        when(userDetailsImpl.getUsername()).thenReturn(mockUsername);

        String resToken = jwtUtils.generateJwtToken(authentication);
        String res = jwtUtils.getUserNameFromJwtToken(resToken);

        assertEquals(mockUsername, res);

        verify(authentication).getPrincipal();
        verify(userDetailsImpl).getUsername();
    }

    @Test
    void testValidateJwtToken() {
        String validToken = Jwts.builder()
        .setSubject("username")
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();

        assertTrue(jwtUtils.validateJwtToken(validToken));
    }
    @Test
    void testValidateJwtTokenErrorSignature() {
        // Utiliser un mauvais jwtSecret pour simuler une signature incorrecte
        String notValidToken = Jwts.builder()
            .setSubject("username")
            .signWith(SignatureAlgorithm.HS512, "incorrectSecret")
            .compact();
    
        boolean isValid = jwtUtils.validateJwtToken(notValidToken);
    
        assertFalse(isValid);
        // Vous pouvez ajouter une vérification des logs ici si nécessaire
    }
    @Test
    void testValidateJwtTokenErrorMalformed() {
        String notValid = "this.is.a.fake.jwt.token";
    
        boolean isValid = jwtUtils.validateJwtToken(notValid);
    
        assertFalse(isValid);
        // Vous pouvez ajouter une vérification des logs ici si nécessaire
    }
    @Test
    void testValidateJwtTokenErrorExpired() {
        String expiredToken = Jwts.builder()
            .setSubject("username")
            .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Set the token as expired
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();

        assertFalse(jwtUtils.validateJwtToken(expiredToken));
    }
    @Test
    void testValidateJwtTokenErrorEmpty() {
        assertFalse(jwtUtils.validateJwtToken(""));
    }
}
