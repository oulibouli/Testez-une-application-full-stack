package com.openclassrooms.starterjwt.unitaire.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
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
        
    }

    @Test
    void testFindByIdValid() {
        Long mockId = 156L;

        when(userService.findById(mockId)).thenReturn(mockedUser);
        when(userMapper.toDto(mockedUser)).thenReturn(mockedUserDto);

        ResponseEntity<?> res = userController.findById(mockId.toString());

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertTrue(res.getBody() instanceof UserDto);
        
        UserDto resUserDto = (UserDto) res.getBody();

        assertEquals(mockedUserDto, resUserDto);
    }

    @Test
    void testFindByIdIsNull() {
        Long mockId = 156L;
        when(userService.findById(mockId)).thenReturn(null);

        ResponseEntity<?> res = userController.findById(mockId.toString());

        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    void testFindByIdNotValid() {
        String mockId = "156L";

        ResponseEntity<?> res = userController.findById(mockId);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testDeleteByIdValid() {
        Long mockId = 156L;

        when(userService.findById(mockId)).thenReturn(mockedUser);
        when(userDetails.getUsername()).thenReturn("user@test.com");
        when(mockedUser.getEmail()).thenReturn("user@test.com");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<?> res = userController.save(mockId.toString());

        assertEquals(HttpStatus.OK, res.getStatusCode());

        verify(userService).findById(mockId);
        verify(userService).delete(mockId);
    }
    @Test
    void testDeleteByIdIsNull() {
        Long mockId = 156L;

        when(userService.findById(mockId)).thenReturn(null);

        ResponseEntity<?> res = userController.save(mockId.toString());

        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }
    @Test
    void testDeleteByIdNotValid() {
        String mockId = "156L";

        ResponseEntity<?> res = userController.save(mockId);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testDeleteByIdUnauthorized() {
        Long mockId = 156L;

        when(userService.findById(mockId)).thenReturn(mockedUser);
        when(userDetails.getUsername()).thenReturn("user");
        when(mockedUser.getEmail()).thenReturn("user@test.com");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<?> res = userController.save(mockId.toString());

        assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());

        verify(userService).findById(mockId);
    }
}
