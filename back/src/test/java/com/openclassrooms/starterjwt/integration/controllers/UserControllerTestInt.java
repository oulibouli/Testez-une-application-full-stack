package com.openclassrooms.starterjwt.integration.controllers;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTestInt {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        // Clear previous data before each test
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding a user by valid ID should return the user")
    @WithMockUser(username = "user@test.com")
    void testFindById_validId_shouldReturnUser() throws Exception {
        // Arrange: create and save a user
        User user = new User();
        user
            .setEmail("user@test.com")
            .setFirstName("firstname")
            .setLastName("lastName");
        userRepository.save(user);

        UserDto expectedUserDto = userMapper.toDto(user);

        // Act & Assert: perform GET request and verify the response
        mockMvc.perform(get("/api/user/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(expectedUserDto.getEmail())))
                .andExpect(jsonPath("$.firstName", is(expectedUserDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(expectedUserDto.getLastName())));
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding a user by invalid ID should return Bad Request")
    @WithMockUser(username = "user@test.com")
    void testFindById_notValidId_shouldReturnBadRequest() throws Exception {
        // Act & Assert: perform GET request with an invalid ID and expect Bad Request status
       mockMvc.perform(get("/api/user/invalid")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Test deleting a user by valid ID should return status OK")
    @WithMockUser(username = "user@test.com")
    void testDeleteById_validId_shouldReturnOk() throws Exception {
        // Arrange: create and save a user
        User user = new User();
        user
            .setEmail("user@test.com")
            .setFirstName("firstName")
            .setLastName("lastName");
        userRepository.save(user);

        // Act & Assert: perform DELETE request and verify the user is deleted
        mockMvc.perform(delete("/api/user/" + user.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Assert: verify the user no longer exists in the repository
        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Test deleting a user by invalid ID should return Bad Request")
    @WithMockUser(username = "user@test.com")
    void testDeleteById_validId_shouldReturnBadRequest() throws Exception {
        // Act & Assert: perform DELETE request with an invalid ID and expect Bad Request status
        mockMvc.perform(delete("/api/user/invalid")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}