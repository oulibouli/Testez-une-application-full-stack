package com.openclassrooms.starterjwt.unitaire.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @Tag("Security")
    @DisplayName("Test JWT filter with valid token should authenticate the user")
    void testDoFilterInternal()  throws ServletException, IOException {
        // Arrange: Set up mock responses
        String mockUsername = "username";
        String mockJwt = "token";
        
        // Mock the header with the JWT
        when(request.getHeader("Authorization")).thenReturn("Bearer " + mockJwt);
        // Mock the JWT validation to return true
        when(jwtUtils.validateJwtToken(mockJwt)).thenReturn(true);
        // Mock the extraction of the username from the JWT
        when(jwtUtils.getUserNameFromJwtToken(mockJwt)).thenReturn(mockUsername);
        // Mock loading the user details by username
        when(userDetailsService.loadUserByUsername(mockUsername)).thenReturn(userDetails);

        // Act: Execute the filter
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert: Verify the interactions
        verify(jwtUtils).validateJwtToken(mockJwt);
        verify(jwtUtils).getUserNameFromJwtToken(mockJwt);
        verify(userDetailsService).loadUserByUsername(mockUsername);
        verify(filterChain).doFilter(request, response);

        // Check the SecurityContextHolder to see if the authentication was set
        UsernamePasswordAuthenticationToken authentication = 
            (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertEquals(userDetails, authentication.getPrincipal());
    }

    @Test
    @Tag("Security")
    @DisplayName("Test JWT filter with invalid token should not authenticate the user")
    void testDoFilterInternalNotOk() throws ServletException, IOException {
        // Arrange: Set up mock responses
        String mockJwt = "invalidJwt";
        String headerAuth = "Bearer " + mockJwt;
        String mockUsername = "username";

        // Mock the header with the invalid JWT
        when(request.getHeader("Authorization")).thenReturn(headerAuth);
        // Simulate an exception during JWT validation
        when(jwtUtils.validateJwtToken(mockJwt)).thenThrow(new RuntimeException("Cannot set user authentication"));

        // Act: Execute the filter
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert: Verify that the filter chain was NOT called since an exception occurred
        verify(userDetailsService, never()).loadUserByUsername(mockUsername);
    }

    @Test
    @Tag("Security")
    @DisplayName("Test JWT filter with no Authorization header should pass the request to the next filter")
    void testDoFilterInternal_NoAuthorizationHeader() throws ServletException, IOException {
        // Arrange: No Authorization header
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act: Execute the filter
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert: Verify that the filter chain was called
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @Tag("Security")
    @DisplayName("Test JWT filter with invalid Authorization header format should pass the request to the next filter")
    void testDoFilterInternal_InvalidAuthorizationHeader() throws ServletException, IOException {
        // Arrange: Invalid Authorization header
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcm5hbWU6cGFzc3dvcmQ=");

        // Act: Execute the filter
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert: Verify that the filter chain was called
        verify(filterChain).doFilter(request, response);
    }
}