package com.openclassrooms.starterjwt.unitaire.security.services;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User mockUser;

    @Test
    @Tag("Service")
    @DisplayName("Test loadUserByUsername with valid username should return UserDetails")
    void testLoadUserByUsername() {
        // Arrange: Set up mock responses
        String mockUsername = "username";
        when(userRepository.findByEmail(mockUsername)).thenReturn(Optional.of(mockUser));
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getEmail()).thenReturn("test@test.com");
        when(mockUser.getLastName()).thenReturn("lastname");
        when(mockUser.getFirstName()).thenReturn("firstname");
        when(mockUser.getPassword()).thenReturn("password");

        // Act: Call the method under test
        UserDetails res = userDetailsServiceImpl.loadUserByUsername(mockUsername);

        // Assert: Verify the returned UserDetails
        assertEquals("test@test.com", res.getUsername());
        assertEquals("password", res.getPassword());
    }

    @Test
    @Tag("Service")
    @DisplayName("Test loadUserByUsername with non-existing username should throw UsernameNotFoundException")
    void testLoadUserByUsernameNotFound() {
        // Arrange: Set up mock responses
        String mockUsername = "username";
        when(userRepository.findByEmail(mockUsername)).thenReturn(Optional.empty());

        // Act & Assert: Verify that the method throws UsernameNotFoundException
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsServiceImpl.loadUserByUsername(mockUsername);
        });
    }
}