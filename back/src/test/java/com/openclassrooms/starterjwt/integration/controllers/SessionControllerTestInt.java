package com.openclassrooms.starterjwt.integration.controllers;

import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTestInt {

    @Autowired
    private SessionMapper sessionMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    @Tag("GET")
    @DisplayName("Test finding a session by valid ID should return status OK")
    @WithMockUser(username = "user@test.com")
    void testFindById_validId_shouldReturnOk() throws Exception {
        // Arrange: create and save a session
        Session session = new Session().setName("name").setDate(new Date()).setDescription("description");
        sessionRepository.save(session);
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Act & Assert: perform GET request and verify the response
        mockMvc.perform(get("/api/session/" + session.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(sessionDto.getName())));
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding a session by invalid ID should return status Bad Request")
    @WithMockUser(username = "user@test.com")
    void testFindById_validId_shouldReturnBadRequest() throws Exception {
        // Act & Assert: perform GET request with an invalid ID and expect Bad Request status
        mockMvc.perform(get("/api/session/invalid")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding all sessions should return status OK")
    @WithMockUser(username = "user@test.com")
    void testFindAll_shouldReturnOk() throws Exception {
        // Arrange: clear existing sessions and create new ones
        sessionRepository.deleteAll();  // Clear previous data
        Session sessionUn = new Session().setName("nameUn").setDate(new Date()).setDescription("description");
        Session sessionDeux = new Session().setName("nameDeux").setDate(new Date()).setDescription("description");
        sessionRepository.save(sessionUn);
        sessionRepository.save(sessionDeux);

        // Act & Assert: perform GET request and verify the response contains both sessions
        mockMvc.perform(get("/api/session/")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[?(@.name == '%s')]", "nameUn").exists())
            .andExpect(jsonPath("$[?(@.name == '%s')]", "nameDeux").exists());
    }

    @Test
    @Tag("POST")
    @DisplayName("Test creating a session should return status OK")
    @WithMockUser(username = "user@test.com")
    void testCreate_shouldReturnOk() throws Exception {
        // Arrange: create a SessionDto object
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("New Session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("New Description");

        // Act & Assert: perform POST request to create a session and verify the response
        mockMvc.perform(post("/api/session/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(sessionDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(sessionDto.getName())))
            .andExpect(jsonPath("$.description", is(sessionDto.getDescription())));
    }

    @Test
    @Tag("PUT")
    @DisplayName("Test updating a session should return status OK")
    @WithMockUser(username = "user@test.com")
    void testUpdate_shouldReturnOk() throws Exception {
        // Arrange: create and save a session and a teacher, then update session details
        Session session = new Session().setName("Name").setDate(new Date()).setDescription("Description");
        sessionRepository.save(session);
        Teacher teacher = new Teacher().setFirstName("FirstName").setLastName("LastName");
        teacherRepository.save(teacher);

        SessionDto updatedSessionDto = new SessionDto();
        updatedSessionDto.setName("Updated");
        updatedSessionDto.setDate(new Date());
        updatedSessionDto.setTeacher_id(teacher.getId());
        updatedSessionDto.setDescription("Updated Description");

        // Act & Assert: perform PUT request to update the session and verify the response
        mockMvc.perform(put("/api/session/" + session.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(updatedSessionDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(updatedSessionDto.getName())))
            .andExpect(jsonPath("$.description", is(updatedSessionDto.getDescription())));
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Test deleting a session should return status OK")
    @WithMockUser(username = "user@test.com")
    void testDelete_shouldReturnOk() throws Exception {
        // Arrange: create and save a session
        Session session = new Session().setName("Session to Delete").setDate(new Date()).setDescription("Description");
        sessionRepository.save(session);

        // Act & Assert: perform DELETE request to remove the session and verify it's gone
        mockMvc.perform(delete("/api/session/" + session.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/session/" + session.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @Tag("POST")
    @DisplayName("Test participating in a session should return status OK")
    @WithMockUser(username = "user@test.com")
    void testParticipate_shouldReturnOk() throws Exception {
        // Arrange: create and save a session and a user
        Session session = new Session().setName("Session for Participation").setDate(new Date()).setDescription("Description");
        sessionRepository.save(session);
        User user = new User().setLastName("lastName").setFirstName("firstName").setEmail("email@test.com").setPassword("password").setAdmin(false);
        userRepository.save(user);

        // Act & Assert: perform POST request to participate in the session and verify the response
        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/" + user.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Test cancelling participation in a session should return status OK")
    @WithMockUser(username = "user@test.com")
    void testNoLongerParticipate_shouldReturnOk() throws Exception {
        // Arrange: create a session with a participant user
        Session session = new Session().setName("Session for Participation Removal").setDate(new Date()).setDescription("Description");
        sessionRepository.save(session);
        session.setUsers(new ArrayList<>());
        User user = new User().setLastName("lastName").setFirstName("firstName").setEmail("email1@test.com").setPassword("password").setAdmin(false);
        userRepository.save(user);
        session.getUsers().add(user);
        sessionRepository.save(session);

        // Act & Assert: perform DELETE request to remove the user from session participants
        mockMvc.perform(delete("/api/session/" + session.getId() + "/participate/" + user.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}