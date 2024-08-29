package com.openclassrooms.starterjwt.integration.controllers;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
    @WithMockUser(username = "user@test.com")
    void testFindById_validId_shouldReturnOk() throws Exception {
        Session session = new Session().setName("name").setDate(new Date()).setDescription("description");

        sessionRepository.save(session);

        SessionDto sessionDto = sessionMapper.toDto(session);

        mockMvc.perform(get("/api/session/" + session.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(sessionDto.getName())));
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void testFindById_validId_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/session/invalid")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void testFindAll_shouldReturnOk() throws Exception {
        sessionRepository.deleteAll();  // Clear previous data

        Session sessionUn = new Session().setName("nameUn").setDate(new Date()).setDescription("description");
        Session sessionDeux = new Session().setName("nameDeux").setDate(new Date()).setDescription("description");

        sessionRepository.save(sessionUn);
        sessionRepository.save(sessionDeux);

        mockMvc.perform(get("/api/session/")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[?(@.name == '%s')]", "nameUn").exists())
            .andExpect(jsonPath("$[?(@.name == '%s')]", "nameDeux").exists());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void testCreate_shouldReturnOk() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("New Session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("New Description");

        mockMvc.perform(post("/api/session/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(sessionDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(sessionDto.getName())))
            .andExpect(jsonPath("$.description", is(sessionDto.getDescription())));
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void testUpdate_shouldReturnOk() throws Exception {
        Session session = new Session().setName("Name").setDate(new Date()).setDescription("Description");
        sessionRepository.save(session);

        Teacher teacher = new Teacher().setFirstName("FirstName").setLastName("LastName");
        teacherRepository.save(teacher);

        SessionDto updatedSessionDto = new SessionDto();
        updatedSessionDto.setName("Updated");
        updatedSessionDto.setDate(new Date());
        updatedSessionDto.setTeacher_id(teacher.getId());
        updatedSessionDto.setDescription("Updated Description");

        mockMvc.perform(put("/api/session/" + session.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(updatedSessionDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(updatedSessionDto.getName())))
            .andExpect(jsonPath("$.description", is(updatedSessionDto.getDescription())));
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void testDelete_shouldReturnOk() throws Exception {
        Session session = new Session().setName("Session to Delete").setDate(new Date()).setDescription("Description");
        sessionRepository.save(session);

        mockMvc.perform(delete("/api/session/" + session.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/session/" + session.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void testParticipate_shouldReturnOk() throws Exception {
        Session session = new Session().setName("Session for Participation").setDate(new Date()).setDescription("Description");
        sessionRepository.save(session);

        User user = new User().setLastName("lastName").setFirstName("firstName").setEmail("email@test.com").setPassword("password").setAdmin(false);

        userRepository.save(user);

        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/" + user.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void testNoLongerParticipate_shouldReturnOk() throws Exception {
        Session session = new Session().setName("Session for Participation Removal").setDate(new Date()).setDescription("Description");
        sessionRepository.save(session);

        session.setUsers(new ArrayList<>());
        User user = new User().setLastName("lastName").setFirstName("firstName").setEmail("email1@test.com").setPassword("password").setAdmin(false);

        userRepository.save(user);

        session.getUsers().add(user);
        sessionRepository.save(session);

        mockMvc.perform(delete("/api/session/" + session.getId() + "/participate/" + user.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}