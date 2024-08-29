package com.openclassrooms.starterjwt.integration.controllers;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
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
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void testFindById_validId_shouldReturnUser() throws Exception {
        User user = new User();
        user
            .setEmail("user@test.com")
            .setFirstName("firstname")
            .setLastName("lastName");
        userRepository.save(user);

        UserDto expectedUserDto = userMapper.toDto(user);

        mockMvc.perform(get("/api/user/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(expectedUserDto.getEmail())))
                .andExpect(jsonPath("$.firstName", is(expectedUserDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(expectedUserDto.getLastName())));
    }
    @Test
    @WithMockUser(username = "user@test.com")
    void testFindById_notValidId_shouldReturnBadRequest() throws Exception {
       mockMvc.perform(get("/api/user/invalid")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void testDeleteById_validId_shouldReturnOk() throws Exception {
        User user = new User();
        user
            .setEmail("user@test.com")
            .setFirstName("firstName")
            .setLastName("lastName");
        userRepository.save(user);

        mockMvc.perform(delete("/api/user/" + user.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertTrue(deletedUser.isEmpty());
    }
    @Test
    @WithMockUser(username = "user@test.com")
    void testDeleteById_validId_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/user/invalid")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}
