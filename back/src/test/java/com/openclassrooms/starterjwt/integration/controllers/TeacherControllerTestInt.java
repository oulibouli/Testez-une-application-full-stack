package com.openclassrooms.starterjwt.integration.controllers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTestInt {
    @Autowired
    private MockMvc mockmMvc;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    @WithMockUser(username = "user@test.com")
    void testFindById_validId_shouldReturnTeacher() throws Exception {
        Teacher teacher = new Teacher();
        teacher
            .setFirstName("firstName")
            .setLastName("lastName");
        teacherRepository.save(teacher);
        
        TeacherDto expectedTeacherDto = teacherMapper.toDto(teacher);

        mockmMvc.perform(get("/api/teacher/" + teacher.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName", is(expectedTeacherDto.getFirstName())))
            .andExpect(jsonPath("$.lastName", is(expectedTeacherDto.getLastName())));
    }
    @Test
    @WithMockUser(username = "user@test.com")
    void testFindById_validId_shouldReturnBadRequest() throws Exception {
        mockmMvc.perform(get("/api/teacher/invalid")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(username = "user@test.com")
    void testFindAll_validId_shouldReturnOk() throws Exception {
        Teacher teacherUn = new Teacher().setFirstName("firstName").setLastName("lastName");
        Teacher teacherDeux = new Teacher().setFirstName("firstNameDeux").setLastName("lastNameDeux");
            
        teacherRepository.save(teacherUn);
        teacherRepository.save(teacherDeux);

        mockmMvc.perform(get("/api/teacher/")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[?(@.firstName == '%s' && @.lastName == '%s')]", "firstName", "lastName").exists())
            .andExpect(jsonPath("$[?(@.firstName == '%s' && @.lastName == '%s')]", "firstNameDeux", "lastNameDeux").exists());
    }
}
