package com.openclassrooms.starterjwt.unitaire.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;
    // Create the SessionService instance with mocked dependencies
    @InjectMocks
    private SessionService sessionService;
    @Mock
    private Session session;
    @Mock
    private User mockedUser;


    @BeforeEach
    void setUp() {
        LocalDateTime currentTime = LocalDateTime.now();
        Teacher teacher = new Teacher();

        teacher
            .setId(1L)
            .setCreatedAt(currentTime)
            .setUpdatedAt(currentTime)
            .setLastName("Ouli")
            .setFirstName("Nico");

        session = new Session();
        session.setUsers(new ArrayList<>());
    }

    @Test
    void createSession() {
        // Prepare a session
        when(sessionRepository.save(session)).thenReturn(session);

        Session createdSession = sessionService.create(session);

        verify(sessionRepository).save(session);
        assertEquals(session, createdSession);
    }

    @Test 
    void deleteSession() {
        Session mockedSession = new Session().setName("Name").setDate(new Date()).setDescription("Description");
        mockedSession.setUsers(new ArrayList<>());

        sessionService.delete(mockedSession.getId());

        verify(sessionRepository).deleteById(mockedSession.getId());
    }

    @Test
    void listSessions() {
        List<Session> sessions = new ArrayList<>();

        sessions.add(session);

        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> res = sessionService.findAll();

        verify(sessionRepository).findAll();

        assertEquals(sessions, res);
    }

    @Test
    void participateSession() {

        when(userRepository.findById(mockedUser.getId())).thenReturn(Optional.of(mockedUser));
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        sessionService.participate(session.getId(), mockedUser.getId());

        verify(sessionRepository).findById(session.getId());
        verify(userRepository).findById(mockedUser.getId());

        assertTrue(session.getUsers().contains(mockedUser));
    }
    @Test
    void participateSessionNull() {

        when(userRepository.findById(6L)).thenReturn(Optional.of(mockedUser));
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(session.getId(), 6L);
        });
    }
    @Test
    void participateSessionUserNull() {

        when(userRepository.findById(6L)).thenReturn(Optional.empty());
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(session.getId(), 6L);
        });
    }
    @Test
    void participateSessionAlreadyParticipate() {
        User newMockedUser = new User("email@test.com", "lastName", "firstName", "password", false);
        newMockedUser.setId(67L);

        Session mockedSession = new Session().setName("Name").setDate(new Date()).setDescription("Description");
        mockedSession.setUsers(new ArrayList<>());
        
        // Add the user to the session's user list to simulate participation
        mockedSession.getUsers().add(newMockedUser);

        when(userRepository.findById(67L)).thenReturn(Optional.of(newMockedUser));
        when(sessionRepository.findById(mockedSession.getId())).thenReturn(Optional.of(mockedSession));

        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(mockedSession.getId(), newMockedUser.getId());
        });
    }

    @Test
    void noLongerParticipateSession() {
        User newMockedUser = new User("email@test.com", "lastName", "firstName", "password", false);
        newMockedUser.setId(67L);

        Session mockedSession = new Session().setName("Name").setDate(new Date()).setDescription("Description");
        mockedSession.setUsers(new ArrayList<>());
        
        // Add the user to the session's user list to simulate participation
        mockedSession.getUsers().add(newMockedUser);

        // Mock the session repository to return the session
        when(sessionRepository.findById(mockedSession.getId())).thenReturn(Optional.of(mockedSession));

        // Call the noLongerParticipate method
        sessionService.noLongerParticipate(mockedSession.getId(), newMockedUser.getId());

        // Verify the interactions and state
        verify(sessionRepository).findById(mockedSession.getId());
        assertFalse(mockedSession.getUsers().contains(newMockedUser)); 
        verify(sessionRepository).save(mockedSession);
    }

    @Test
    void noLongerParticipateSessionNull() {
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            sessionService.noLongerParticipate(session.getId(), 6L);
        });
    }

    @Test
    void testGetById() {
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        
        Session res = sessionService.getById(session.getId());

        verify(sessionRepository).findById(session.getId());

        assertEquals(session, res);
    }

    @Test
    void testUpdate() {
        when(sessionRepository.save(session)).thenReturn(session);

        Session res = sessionService.update(session.getId(), session);

        verify(sessionRepository).save(session);

        assertEquals(session, res);
    }
}
