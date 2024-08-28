package com.openclassrooms.starterjwt.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;
    private SessionService sessionService;
    private Session session;
    private User mockedUser;


    @BeforeEach
    void setUp() {
        LocalDateTime currentTime = LocalDateTime.now();
        Date currentDate = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
        // Create the SessionService instance with mocked dependencies
        sessionService = new SessionService(sessionRepository, userRepository);

        Teacher teacher = new Teacher();

        teacher
            .setId(1L)
            .setCreatedAt(currentTime)
            .setUpdatedAt(currentTime)
            .setLastName("Ouli")
            .setFirstName("Nico");

        mockedUser = mock(User.class);  // Mockito mock creation
       

        List<User> users = new ArrayList<>();

        session = new Session(1L, "TEST session",
                currentDate, "TEST description for the session",
                teacher,
                users, currentTime,
                currentTime);
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
        doNothing().when(sessionRepository).deleteById(session.getId());
        sessionService.delete(session.getId());

        verify(sessionRepository).deleteById(session.getId());

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

        when(userRepository.findById(6L)).thenReturn(Optional.of(mockedUser));
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        sessionService.participate(session.getId(), 6L);

        verify(sessionRepository).findById(session.getId());
        verify(userRepository).findById(6L);

        assertTrue(session.getUsers().contains(mockedUser));
    }

    @Test
    void noLongerParticipateSession() {
        User newMockedUser = new User("Toto2", "Toto2",
                "Toto420", "totoBlud", false);
        newMockedUser.setId(67L);

        session.getUsers().add(newMockedUser);

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);

        sessionService.noLongerParticipate(session.getId(), 67L);

        verify(sessionRepository).findById(1L);
        assertFalse(session.getUsers().contains(mockedUser));
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
