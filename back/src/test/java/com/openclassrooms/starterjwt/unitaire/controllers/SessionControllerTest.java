package com.openclassrooms.starterjwt.unitaire.controllers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    }

    @Test
    void testFindByIdNotNull() {
        Long mockId = 645L;
        when(sessionService.getById(mockId)).thenReturn(mockedSession);
        ResponseEntity<?> res = sessionController.findById(mockId.toString());
        assertEquals(HttpStatus.OK, res.getStatusCode());

        verify(sessionService).getById(mockId);
    }
    @Test
    void testFindByIdNull() {
        Long mockId = 645L;
        when(sessionService.getById(mockId)).thenReturn(null);
        ResponseEntity<?> res = sessionController.findById(mockId.toString());
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());

        verify(sessionService).getById(mockId);
    }
    @Test
    void testFindByIdNotValid() {
        String mockId = "645L";

        ResponseEntity<?> res = sessionController.findById(mockId);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testFindAll() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(mockedSession);
        List<SessionDto> sessionDtos = new ArrayList<>();
        sessionDtos.add(mockedSessionDto);

        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        when(sessionService.findAll()).thenReturn(sessions);

        ResponseEntity<?> res = sessionController.findAll();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<SessionDto> result = (List<SessionDto>) res.getBody();
        assertEquals(sessionDtos, result);

        verify(sessionService).findAll(); 
        verify(sessionMapper).toDto(sessions); 
    }

    @Test
    void testCreate() {
        when(sessionService.create(sessionMapper.toEntity(mockedSessionDto))).thenReturn(mockedSession);
        when(sessionMapper.toDto(mockedSession)).thenReturn(mockedSessionDto);

        ResponseEntity<?> res = sessionController.create(mockedSessionDto);

        SessionDto resDto = (SessionDto) res.getBody();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(mockedSessionDto, resDto);


        verify(sessionMapper).toDto(mockedSession); 
    }

    @Test
    void testUpdate() {
        when(sessionMapper.toDto(mockedSession)).thenReturn(mockedSessionDto);
        when(sessionService.update(mockedSessionDto.getId(), sessionMapper.toEntity(mockedSessionDto))).thenReturn(mockedSession);
    
        ResponseEntity<?> res = sessionController.update(mockedSessionDto.getId().toString(), mockedSessionDto);
    
        assertEquals(HttpStatus.OK, res.getStatusCode());
        SessionDto resDto = (SessionDto) res.getBody();

        assertEquals(mockedSessionDto, resDto);

        verify(sessionMapper).toDto(mockedSession); 
        verify(sessionService).update(mockedSessionDto.getId(), sessionMapper.toEntity(mockedSessionDto));
    }
    
    @Test
    void testUpdateNotValid() {
        String mockId = "invalid";
        ResponseEntity<?> res = sessionController.update(mockId, mockedSessionDto);
    
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testDeleteByIdValid() {
        when(sessionService.getById(mockedSession.getId())).thenReturn(mockedSession);

        ResponseEntity<?> res = sessionController.save(mockedSession.getId().toString());

        assertEquals(HttpStatus.OK, res.getStatusCode());
        verify(sessionService).delete(mockedSession.getId());
    }
    @Test
    void testDeleteByIdNull() {
        when(sessionService.getById(mockedSession.getId())).thenReturn(null);

        ResponseEntity<?> res = sessionController.save(mockedSession.getId().toString());

        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }
    @Test
    void testDeleteByIdNotValid() {
        String mockId = "invalid";

        ResponseEntity<?> res = sessionController.save(mockId);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testParticipateValid() {
    
        ResponseEntity<?> res = sessionController.participate(mockedSession.getId().toString(), mockedUser.getId().toString());

        assertEquals(HttpStatus.OK, res.getStatusCode());

        verify(sessionService).participate(mockedSession.getId(), mockedUser.getId());
    }

    @Test
    void testParticipateNotValid() {
        String mockId = "invalid";
        ResponseEntity<?> res = sessionController.participate(mockId, mockedUser.getId().toString());

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testNoLongerParticipateValid() {
        ResponseEntity<?> res = sessionController.noLongerParticipate(mockedSession.getId().toString(), mockedUser.getId().toString());

        assertEquals(HttpStatus.OK, res.getStatusCode());

        verify(sessionService).noLongerParticipate(mockedSession.getId(), mockedUser.getId());
    }
    
    @Test
    void testNoLongerParticipateNotValid() {
        String mockId = "invalid";
        ResponseEntity<?> res = sessionController.noLongerParticipate(mockId, mockedUser.getId().toString());

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }
}
