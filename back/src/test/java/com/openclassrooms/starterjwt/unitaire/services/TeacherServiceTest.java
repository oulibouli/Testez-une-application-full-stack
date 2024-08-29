package com.openclassrooms.starterjwt.unitaire.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
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
    }

    @Test
    void testFindById() {
        Long mockId = 456L;

        when(teacherRepository.findById(mockId)).thenReturn(Optional.of(mockedTeacher));

        Teacher res = teacherService.findById(mockId);

        verify(teacherRepository).findById(mockId);

        assertEquals(mockedTeacher, res);
    }

    @Test
    void findAll() {
        List<Teacher> teachers = new ArrayList<>();

        teachers.add(mockedTeacher);
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> res = teacherService.findAll();

        verify(teacherRepository).findAll();
        
        assertEquals(teachers, res);
    }
}
