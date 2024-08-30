package com.openclassrooms.starterjwt.unitaire.services;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    // Create the service instance with mocked dependencies
    @InjectMocks
    private UserService userService;

    @Mock
    private User mockedUser;

    @BeforeEach
    void setUp() {
        // No specific setup required for these tests
    }

    @Test
    @Tag("Delete")
    @DisplayName("Test delete should remove the user by ID")
    void testDelete() {
        // Arrange: Set up a mock user ID
        Long mockId = 4654L;
        
        // Act: Call the delete method
        userService.delete(mockId);
        
        // Assert: Verify that the user was deleted by ID
        verify(userRepository).deleteById(mockId);
    }

    @Test
    @Tag("Find")
    @DisplayName("Test findById should return the correct user")
    void testFindById() {
        // Arrange: Set up a mock user ID and return value
        Long mockId = 4654L;
        when(userRepository.findById(mockId)).thenReturn(Optional.of(mockedUser));

        // Act: Call the findById method
        User res = userService.findById(mockId);

        // Assert: Verify that the user was returned correctly
        verify(userRepository).findById(mockId);
        assertEquals(mockedUser, res);
    }
}