package com.openclassrooms.starterjwt.unitaire.services;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
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
    }

    @Test
    void testDelete() {
        Long mockId = 4654L;
        
        userService.delete(mockId);
        verify(userRepository).deleteById(mockId);
    }

    @Test
    void testFindById() {
        Long mockId = 4654L;

        when(userRepository.findById(mockId)).thenReturn(Optional.of(mockedUser));

        User res = userService.findById(mockId);

        verify(userRepository).findById(mockId);

        assertEquals(mockedUser, res);
    }
}
