package com.openclassrooms.starterjwt.controllers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    TeacherController teacherController;

    @BeforeEach
    void setUp() {
        teacherController = new TeacherController(teacherService, teacherMapper);
    }

    @Test
    void testFindByIdValid() {
        Long mockId = 456L;
        when(teacherService.findById(mockId)).thenReturn(mockedTeacher);
        when(teacherMapper.toDto(mockedTeacher)).thenReturn(mockedTeacherDto);

        ResponseEntity<?> res = teacherController.findById(mockId.toString());

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertTrue(res.getBody() instanceof TeacherDto);

        TeacherDto resultTeacherDto = (TeacherDto) res.getBody();

        assertEquals(resultTeacherDto, resultTeacherDto);
    }

    @Test
    void testFindByIdIsNull() {
        Long mockId = 456L;
        when(teacherService.findById(mockId)).thenReturn(null);

        ResponseEntity<?> res = teacherController.findById(mockId.toString());

        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }
    @Test
    void testFindByIdNotValid() {
        String mockId = "456L";

        ResponseEntity<?> res = teacherController.findById(mockId);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void testFindAll() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(mockedTeacher);

        List<TeacherDto> dtoTeachers = new ArrayList<>();
        dtoTeachers.add(mockedTeacherDto);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(dtoTeachers);

        ResponseEntity<?> res = teacherController.findAll();

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), dtoTeachers);
    }
}
