package com.openclassrooms.starterjwt.unitaire.controllers;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;

@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    // Create the instance with mocked dependencies
    @InjectMocks
    private SessionController sessionController;

    @Mock
    private SessionDto mockedSessionDto;

    @Mock
    private Session mockedSession;

    @Mock
    private User mockedUser;

    @BeforeEach
    void setUp() {
        // No specific setup required for these tests
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding a session by valid ID should return the session")
    void testFindByIdNotNull() {
        Long mockId = 645L;
        when(sessionService.getById(mockId)).thenReturn(mockedSession);

        // Act: Call the method under test
        ResponseEntity<?> res = sessionController.findById(mockId.toString());

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, res.getStatusCode());

        // Verify interactions with the mock
        verify(sessionService).getById(mockId);
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding a session by ID that does not exist should return NOT FOUND")
    void testFindByIdNull() {
        Long mockId = 645L;
        when(sessionService.getById(mockId)).thenReturn(null);

        // Act: Call the method under test
        ResponseEntity<?> res = sessionController.findById(mockId.toString());

        // Assert: Verify the response
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());

        // Verify interactions with the mock
        verify(sessionService).getById(mockId);
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding a session by invalid ID should return BAD REQUEST")
    void testFindByIdNotValid() {
        String mockId = "645L";

        // Act: Call the method under test
        ResponseEntity<?> res = sessionController.findById(mockId);

        // Assert: Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding all sessions should return a list of session DTOs")
    void testFindAll() {
        // Arrange: Set up mock responses
        List<Session> sessions = new ArrayList<>();
        sessions.add(mockedSession);
        List<SessionDto> sessionDtos = new ArrayList<>();
        sessionDtos.add(mockedSessionDto);

        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);
        when(sessionService.findAll()).thenReturn(sessions);

        // Act: Call the method under test
        ResponseEntity<?> res = sessionController.findAll();

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<SessionDto> result = (List<SessionDto>) res.getBody();
        assertEquals(sessionDtos, result);

        // Verify interactions with the mocks
        verify(sessionService).findAll(); 
        verify(sessionMapper).toDto(sessions); 
    }

    @Test
    @Tag("POST")
    @DisplayName("Test creating a session should return the created session DTO")
    void testCreate() {
        // Arrange: Set up mock responses
        when(sessionService.create(sessionMapper.toEntity(mockedSessionDto))).thenReturn(mockedSession);
        when(sessionMapper.toDto(mockedSession)).thenReturn(mockedSessionDto);

        // Act: Call the method under test
        ResponseEntity<?> res = sessionController.create(mockedSessionDto);

        // Assert: Verify the response
        SessionDto resDto = (SessionDto) res.getBody();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(mockedSessionDto, resDto);

        // Verify interactions with the mocks
        verify(sessionMapper).toDto(mockedSession); 
    }

    @Test
    @Tag("PUT")
    @DisplayName("Test updating a session should return the updated session DTO")
    void testUpdate() {
        // Arrange: Set up mock responses
        when(sessionMapper.toDto(mockedSession)).thenReturn(mockedSessionDto);
        when(sessionService.update(mockedSessionDto.getId(), sessionMapper.toEntity(mockedSessionDto))).thenReturn(mockedSession);
    
        // Act: Call the method under test
        ResponseEntity<?> res = sessionController.update(mockedSessionDto.getId().toString(), mockedSessionDto);
    
        // Assert: Verify the response
        assertEquals(HttpStatus.OK, res.getStatusCode());
        SessionDto resDto = (SessionDto) res.getBody();
        assertEquals(mockedSessionDto, resDto);

        // Verify interactions with the mocks
        verify(sessionMapper).toDto(mockedSession); 
        verify(sessionService).update(mockedSessionDto.getId(), sessionMapper.toEntity(mockedSessionDto));
    }
    
    @Test
    @Tag("PUT")
    @DisplayName("Test updating a session with invalid ID should return BAD REQUEST")
    void testUpdateNotValid() {
        String mockId = "invalid";

        // Act: Call the method under test
        ResponseEntity<?> res = sessionController.update(mockId, mockedSessionDto);
    
        // Assert: Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Test deleting a session by valid ID should return OK")
    void testDeleteByIdValid() {
        when(sessionService.getById(mockedSession.getId())).thenReturn(mockedSession);

        // Act: Call the method under test
        ResponseEntity<?> res = sessionController.save(mockedSession.getId().toString());

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, res.getStatusCode());

        // Verify interactions with the mock
        verify(sessionService).delete(mockedSession.getId());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Test deleting a session by ID that does not exist should return NOT FOUND")
    void testDeleteByIdNull() {
        when(sessionService.getById(mockedSession.getId())).thenReturn(null);

        // Act: Call the method under test
        ResponseEntity<?> res = sessionController.save(mockedSession.getId().toString());

        // Assert: Verify the response
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Test deleting a session with invalid ID should return BAD REQUEST")
    void testDeleteByIdNotValid() {
        String mockId = "invalid";

        // Act: Call the method under test
        ResponseEntity<?> res = sessionController.save(mockId);

        // Assert: Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    @Tag("POST")
    @DisplayName("Test participating in a session with valid IDs should return OK")
    void testParticipateValid() {
        // Act: Call the method under test
        ResponseEntity<?> res = sessionController.participate(mockedSession.getId().toString(), mockedUser.getId().toString());

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, res.getStatusCode());

        // Verify interactions with the mock
        verify(sessionService).participate(mockedSession.getId(), mockedUser.getId());
    }

    @Test
    @Tag("POST")
    @DisplayName("Test participating in a session with invalid session ID should return BAD REQUEST")
    void testParticipateNotValid() {
        String mockId = "invalid";

        // Act: Call the method under test
        ResponseEntity<?> res = sessionController.participate(mockId, mockedUser.getId().toString());

        // Assert: Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Test cancelling participation in a session with valid IDs should return OK")
    void testNoLongerParticipateValid() {
        // Act: Call the method under test
        ResponseEntity<?> res = sessionController.noLongerParticipate(mockedSession.getId().toString(), mockedUser.getId().toString());

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, res.getStatusCode());

        // Verify interactions with the mock
        verify(sessionService).noLongerParticipate(mockedSession.getId(), mockedUser.getId());
    }
    
    @Test
    @Tag("DELETE")
    @DisplayName("Test cancelling participation in a session with invalid session ID should return BAD REQUEST")
    void testNoLongerParticipateNotValid() {
        String mockId = "invalid";

        // Act: Call the method under test
        ResponseEntity<?> res = sessionController.noLongerParticipate(mockId, mockedUser.getId().toString());

        // Assert: Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }
}