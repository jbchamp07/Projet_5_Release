package com.openclassrooms.starterjwt.controllers;


import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TeacherControllerTest {
    private MockMvc mockMvc;
    private Teacher teacher;
    private TeacherDto teacherDto;
    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    public TeacherControllerTest() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
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

    //Tests mockmvc
    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());

        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
        teacherDto.setCreatedAt(LocalDateTime.now());
        teacherDto.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void findById_ShouldReturnTeacher_WhenTeacherExists() throws Exception {
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void findById_ShouldReturnNotFound_WhenTeacherDoesNotExist() throws Exception {
        when(teacherService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_ShouldReturnBadRequest_WhenIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/teacher/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_ShouldReturnListOfTeachers() throws Exception {
        List<Teacher> teachers = Arrays.asList(teacher);
        List<TeacherDto> teacherDtos = Arrays.asList(teacherDto);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

}
