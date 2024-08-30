package com.openclassrooms.starterjwt.unitaire.controllers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private Teacher mockedTeacher;

    @Mock
    private TeacherDto mockedTeacherDto;

    // Create the instance with mocked dependencies
    @InjectMocks
    TeacherController teacherController;

    @BeforeEach
    void setUp() {
        // No specific setup required for these tests
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding a teacher by valid ID should return the teacher DTO")
    void testFindByIdValid() {
        // Arrange: Set up mock responses
        Long mockId = 456L;
        when(teacherService.findById(mockId)).thenReturn(mockedTeacher);
        when(teacherMapper.toDto(mockedTeacher)).thenReturn(mockedTeacherDto);

        // Act: Call the method under test
        ResponseEntity<?> res = teacherController.findById(mockId.toString());

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertTrue(res.getBody() instanceof TeacherDto);

        TeacherDto resultTeacherDto = (TeacherDto) res.getBody();

        assertEquals(mockedTeacherDto, resultTeacherDto);
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding a teacher by ID that does not exist should return NOT FOUND")
    void testFindByIdIsNull() {
        // Arrange: Set up mock responses
        Long mockId = 456L;
        when(teacherService.findById(mockId)).thenReturn(null);

        // Act: Call the method under test
        ResponseEntity<?> res = teacherController.findById(mockId.toString());

        // Assert: Verify the response
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding a teacher by invalid ID should return BAD REQUEST")
    void testFindByIdNotValid() {
        // Arrange: Prepare an invalid ID
        String mockId = "456L";

        // Act: Call the method under test
        ResponseEntity<?> res = teacherController.findById(mockId);

        // Assert: Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding all teachers should return a list of teacher DTOs")
    void testFindAll() {
        // Arrange: Set up mock responses
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(mockedTeacher);

        List<TeacherDto> dtoTeachers = new ArrayList<>();
        dtoTeachers.add(mockedTeacherDto);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(dtoTeachers);

        // Act: Call the method under test
        ResponseEntity<?> res = teacherController.findAll();

        // Assert: Verify the response
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), dtoTeachers);
    }
}