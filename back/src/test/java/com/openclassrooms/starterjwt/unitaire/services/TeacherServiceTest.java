package com.openclassrooms.starterjwt.unitaire.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private Teacher mockedTeacher;

    // Create the service instance with mocked dependencies
    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        // No specific setup required for these tests
    }

    @Test
    @Tag("Find")
    @DisplayName("Test findById should return the correct teacher")
    void testFindById() {
        // Arrange: Set up mock responses
        Long mockId = 456L;
        when(teacherRepository.findById(mockId)).thenReturn(Optional.of(mockedTeacher));

        // Act: Call the findById method
        Teacher res = teacherService.findById(mockId);

        // Assert: Verify that the teacher was returned correctly
        verify(teacherRepository).findById(mockId);
        assertEquals(mockedTeacher, res);
    }

    @Test
    @Tag("Find")
    @DisplayName("Test findAll should return a list of all teachers")
    void findAll() {
        // Arrange: Prepare a list of teachers
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(mockedTeacher);
        when(teacherRepository.findAll()).thenReturn(teachers);

        // Act: Call the findAll method
        List<Teacher> res = teacherService.findAll();

        // Assert: Verify that the list of teachers was returned correctly
        verify(teacherRepository).findAll();
        assertEquals(teachers, res);
    }
}