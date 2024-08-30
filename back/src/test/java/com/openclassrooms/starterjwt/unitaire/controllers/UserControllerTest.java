package com.openclassrooms.starterjwt.unitaire.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    // Create the instance with mocked dependencies
    @InjectMocks
    private UserController userController;

    @Mock
    private User mockedUser;

    @Mock
    private UserDto mockedUserDto;

    @Mock
    private UserDetails userDetails;

    @Mock
    Authentication authentication;

    @Mock
    SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        // No specific setup required for these tests
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding a user by valid ID should return the user DTO")
    void testFindByIdValid() {
        // Arrange: Set up mock responses
        Long mockId = 156L;
        when(userService.findById(mockId)).thenReturn(mockedUser);
        when(userMapper.toDto(mockedUser)).thenReturn(mockedUserDto);

        // Act: Call the method under test
        ResponseEntity<?> res = userController.findById(mockId.toString());

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertTrue(res.getBody() instanceof UserDto);
        
        UserDto resUserDto = (UserDto) res.getBody();
        assertEquals(mockedUserDto, resUserDto);
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding a user by ID that does not exist should return NOT FOUND")
    void testFindByIdIsNull() {
        // Arrange: Set up mock responses
        Long mockId = 156L;
        when(userService.findById(mockId)).thenReturn(null);

        // Act: Call the method under test
        ResponseEntity<?> res = userController.findById(mockId.toString());

        // Assert: Verify the response
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding a user by invalid ID should return BAD REQUEST")
    void testFindByIdNotValid() {
        // Arrange: Prepare an invalid ID
        String mockId = "156L";

        // Act: Call the method under test
        ResponseEntity<?> res = userController.findById(mockId);

        // Assert: Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Test deleting a user by valid ID with proper authorization should return OK")
    void testDeleteByIdValid() {
        // Arrange: Set up mock responses
        Long mockId = 156L;

        when(userService.findById(mockId)).thenReturn(mockedUser);
        when(userDetails.getUsername()).thenReturn("user@test.com");
        when(mockedUser.getEmail()).thenReturn("user@test.com");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        // Act: Call the method under test
        ResponseEntity<?> res = userController.save(mockId.toString());

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, res.getStatusCode());

        // Verify interactions with the mock
        verify(userService).findById(mockId);
        verify(userService).delete(mockId);
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Test deleting a user by ID that does not exist should return NOT FOUND")
    void testDeleteByIdIsNull() {
        // Arrange: Set up mock responses
        Long mockId = 156L;

        when(userService.findById(mockId)).thenReturn(null);

        // Act: Call the method under test
        ResponseEntity<?> res = userController.save(mockId.toString());

        // Assert: Verify the response
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Test deleting a user by invalid ID should return BAD REQUEST")
    void testDeleteByIdNotValid() {
        // Arrange: Prepare an invalid ID
        String mockId = "156L";

        // Act: Call the method under test
        ResponseEntity<?> res = userController.save(mockId);

        // Assert: Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Test deleting a user by valid ID with improper authorization should return UNAUTHORIZED")
    void testDeleteByIdUnauthorized() {
        // Arrange: Set up mock responses
        Long mockId = 156L;

        when(userService.findById(mockId)).thenReturn(mockedUser);
        when(userDetails.getUsername()).thenReturn("user");
        when(mockedUser.getEmail()).thenReturn("user@test.com");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        // Act: Call the method under test
        ResponseEntity<?> res = userController.save(mockId.toString());

        // Assert: Verify the response
        assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());

        // Verify interactions with the mock
        verify(userService).findById(mockId);
    }
}