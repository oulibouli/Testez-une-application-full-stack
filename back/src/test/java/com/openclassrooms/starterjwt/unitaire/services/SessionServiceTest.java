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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
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
    @Tag("Create")
    @DisplayName("Test createSession should save and return the session")
    void createSession() {
        // Arrange: Prepare a session
        when(sessionRepository.save(session)).thenReturn(session);

        // Act: Call the create method
        Session createdSession = sessionService.create(session);

        // Assert: Verify that the session was saved and returned correctly
        verify(sessionRepository).save(session);
        assertEquals(session, createdSession);
    }

    @Test 
    @Tag("Delete")
    @DisplayName("Test deleteSession should remove the session by ID")
    void deleteSession() {
        // Arrange: Prepare a session
        Session mockedSession = new Session().setName("Name").setDate(new Date()).setDescription("Description");
        mockedSession.setUsers(new ArrayList<>());

        // Act: Call the delete method
        sessionService.delete(mockedSession.getId());

        // Assert: Verify that the session was deleted by ID
        verify(sessionRepository).deleteById(mockedSession.getId());
    }

    @Test
    @Tag("Read")
    @DisplayName("Test listSessions should return all sessions")
    void listSessions() {
        // Arrange: Prepare a list of sessions
        List<Session> sessions = new ArrayList<>();
        sessions.add(session);

        when(sessionRepository.findAll()).thenReturn(sessions);

        // Act: Call the findAll method
        List<Session> res = sessionService.findAll();

        // Assert: Verify that the list of sessions was returned correctly
        verify(sessionRepository).findAll();
        assertEquals(sessions, res);
    }

    @Test
    @Tag("Participate")
    @DisplayName("Test participateSession should add user to session participants")
    void participateSession() {
        // Arrange: Set up mock responses
        when(userRepository.findById(mockedUser.getId())).thenReturn(Optional.of(mockedUser));
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        // Act: Call the participate method
        sessionService.participate(session.getId(), mockedUser.getId());

        // Assert: Verify that the user was added to the session participants
        verify(sessionRepository).findById(session.getId());
        verify(userRepository).findById(mockedUser.getId());
        assertTrue(session.getUsers().contains(mockedUser));
    }

    @Test
    @Tag("Participate")
    @DisplayName("Test participateSession with null session should throw NotFoundException")
    void participateSessionNull() {
        // Arrange: Set up mock responses for a null session
        when(userRepository.findById(6L)).thenReturn(Optional.of(mockedUser));
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

        // Act & Assert: Verify that a NotFoundException is thrown
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(session.getId(), 6L);
        });
    }

    @Test
    @Tag("Participate")
    @DisplayName("Test participateSession with null user should throw NotFoundException")
    void participateSessionUserNull() {
        // Arrange: Set up mock responses for a null user
        when(userRepository.findById(6L)).thenReturn(Optional.empty());
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        // Act & Assert: Verify that a NotFoundException is thrown
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(session.getId(), 6L);
        });
    }

    @Test
    @Tag("Participate")
    @DisplayName("Test participateSession with already participating user should throw BadRequestException")
    void participateSessionAlreadyParticipate() {
        // Arrange: Create a user and add them to the session
        User newMockedUser = new User("email@test.com", "lastName", "firstName", "password", false);
        newMockedUser.setId(67L);

        Session mockedSession = new Session().setName("Name").setDate(new Date()).setDescription("Description");
        mockedSession.setUsers(new ArrayList<>());
        mockedSession.getUsers().add(newMockedUser);

        // Set up mock responses
        when(userRepository.findById(67L)).thenReturn(Optional.of(newMockedUser));
        when(sessionRepository.findById(mockedSession.getId())).thenReturn(Optional.of(mockedSession));

        // Act & Assert: Verify that a BadRequestException is thrown
        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(mockedSession.getId(), newMockedUser.getId());
        });
    }

    @Test
    @Tag("Unparticipate")
    @DisplayName("Test noLongerParticipateSession should remove user from session participants")
    void noLongerParticipateSession() {
        // Arrange: Create a user and add them to the session
        User newMockedUser = new User("email@test.com", "lastName", "firstName", "password", false);
        newMockedUser.setId(67L);

        Session mockedSession = new Session().setName("Name").setDate(new Date()).setDescription("Description");
        mockedSession.setUsers(new ArrayList<>());
        mockedSession.getUsers().add(newMockedUser);

        // Set up mock responses
        when(sessionRepository.findById(mockedSession.getId())).thenReturn(Optional.of(mockedSession));

        // Act: Call the noLongerParticipate method
        sessionService.noLongerParticipate(mockedSession.getId(), newMockedUser.getId());

        // Assert: Verify that the user was removed from the session participants
        verify(sessionRepository).findById(mockedSession.getId());
        assertFalse(mockedSession.getUsers().contains(newMockedUser)); 
        verify(sessionRepository).save(mockedSession);
    }

    @Test
    @Tag("Unparticipate")
    @DisplayName("Test noLongerParticipateSession with null session should throw NotFoundException")
    void noLongerParticipateSessionNull() {
        // Arrange: Set up mock responses for a null session
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

        // Act & Assert: Verify that a NotFoundException is thrown
        assertThrows(NotFoundException.class, () -> {
            sessionService.noLongerParticipate(session.getId(), 6L);
        });
    }

    @Test
    @Tag("Read")
    @DisplayName("Test getById should return the session")
    void testGetById() {
        // Arrange: Set up mock responses
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        
        // Act: Call the getById method
        Session res = sessionService.getById(session.getId());

        // Assert: Verify that the session was returned correctly
        verify(sessionRepository).findById(session.getId());
        assertEquals(session, res);
    }

    @Test
    @Tag("Update")
    @DisplayName("Test update should save and return the updated session")
    void testUpdate() {
        // Arrange: Set up mock responses
        when(sessionRepository.save(session)).thenReturn(session);

        // Act: Call the update method
        Session res = sessionService.update(session.getId(), session);

        // Assert: Verify that the session was updated and returned correctly
        verify(sessionRepository).save(session);
        assertEquals(session, res);
    }
}