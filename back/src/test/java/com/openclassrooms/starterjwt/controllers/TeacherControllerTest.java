package com.openclassrooms.starterjwt.controllers;


import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    public TeacherControllerTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById_Ok() {
        Long id = (long)1;
        Teacher teacher = new Teacher();
        teacher.setId(id);

        when(teacherService.findById(id)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(null); // Remplacez null par le résultat attendu

        ResponseEntity<?> responseEntity = teacherController.findById(id.toString());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(teacherService, times(1)).findById(id);
        verify(teacherMapper, times(1)).toDto(teacher);
    }
    @Test
    public void testFindById_NotFound() {

        when(teacherService.findById((long)1)).thenReturn(null);

        ResponseEntity<?> responseEntity = teacherController.findById("1");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    @Test
    public void testFindById_BadRequest() {
        doThrow(new NumberFormatException()).when(teacherService).findById((long) 1);

        ResponseEntity<?> responseEntity = teacherController.findById("1");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testFindAll() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher());

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(new ArrayList<>()); // Remplacez null par le résultat attendu

        ResponseEntity<?> responseEntity = teacherController.findAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(teacherService, times(1)).findAll();
        verify(teacherMapper, times(1)).toDto(teachers);
    }

    // Ajoutez d'autres méthodes de test pour les autres méthodes du contrôleur

}
