package com.openclassrooms.starterjwt.mapper;


import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class SessionMapperTest {


    @InjectMocks
    private SessionMapperImpl sessionMapper;
    @Mock
    private TeacherService teacherService;

    public SessionMapperTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSessionDtoToEntity(){
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId((long)1);
        sessionDto.setName("Name");
        sessionDto.setCreatedAt(LocalDateTime.now());
        sessionDto.setUpdatedAt(LocalDateTime.now());;
        sessionDto.setUsers(new ArrayList<>());
        sessionDto.setDescription("Description");
        sessionDto.setTeacher_id((long)2);
        sessionDto.setDate(Date.from(Instant.now()));

        Teacher teacher = new Teacher().setId((long)2);
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);

        Session session = sessionMapper.toEntity(sessionDto);
        assertEquals(sessionDto.getId(),session.getId());
        assertEquals(sessionDto.getName(),session.getName());
        assertEquals(sessionDto.getDate(),session.getDate());
        assertEquals(sessionDto.getCreatedAt(),session.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(),session.getUpdatedAt());
        assertEquals(sessionDto.getUsers(),session.getUsers());
        assertEquals(sessionDto.getDescription(),session.getDescription());
        assertEquals(sessionDto.getTeacher_id(),session.getTeacher().getId());
    }

    @Test
    public void testSessionToDto(){
        Teacher teacher = new Teacher().setId((long)2);
        Session session = new Session();
        session.setId((long)1);
        session.setName("Name");
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());;
        session.setUsers(new ArrayList<>());
        session.setDescription("Description");
        session.setTeacher(teacher);
        session.setDate(Date.from(Instant.now()));


        SessionDto sessionDto = sessionMapper.toDto(session);
        assertEquals(session.getId(),sessionDto.getId());
        assertEquals(session.getName(),sessionDto.getName());
        assertEquals(session.getDate(),sessionDto.getDate());
        assertEquals(session.getCreatedAt(),sessionDto.getCreatedAt());
        assertEquals(session.getUpdatedAt(),sessionDto.getUpdatedAt());
        assertEquals(session.getUsers(),sessionDto.getUsers());
        assertEquals(session.getDescription(),sessionDto.getDescription());
        assertEquals(session.getTeacher().getId(),sessionDto.getTeacher_id());
    }

    @Test
    public void testListToDto(){
        Teacher teacher = new Teacher().setId((long)2);
        Session session = new Session();
        session.setId((long)1);
        session.setName("Name");
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());;
        session.setUsers(new ArrayList<>());
        session.setDescription("Description");
        session.setTeacher(teacher);
        session.setDate(Date.from(Instant.now()));
        List<Session> list = new ArrayList<Session>();
        list.add(session);


        List<SessionDto> listDto = sessionMapper.toDto(list);
        SessionDto sessionDto = listDto.get(0);

        assertEquals(session.getId(),sessionDto.getId());
        assertEquals(session.getName(),sessionDto.getName());
        assertEquals(session.getDate(),sessionDto.getDate());
        assertEquals(session.getCreatedAt(),sessionDto.getCreatedAt());
        assertEquals(session.getUpdatedAt(),sessionDto.getUpdatedAt());
        assertEquals(session.getUsers(),sessionDto.getUsers());
        assertEquals(session.getDescription(),sessionDto.getDescription());
        assertEquals(session.getTeacher().getId(),sessionDto.getTeacher_id());
    }

    @Test
    public void testListDtoToEntity(){
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId((long)1);
        sessionDto.setName("Name");
        sessionDto.setCreatedAt(LocalDateTime.now());
        sessionDto.setUpdatedAt(LocalDateTime.now());;
        sessionDto.setUsers(new ArrayList<>());
        sessionDto.setDescription("Description");
        sessionDto.setTeacher_id((long)2);
        sessionDto.setDate(Date.from(Instant.now()));
        List<SessionDto> listDto = new ArrayList<SessionDto>();
        listDto.add(sessionDto);


        Teacher teacher = new Teacher().setId((long)2);
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);
        List<Session> list = sessionMapper.toEntity(listDto);
        Session session = list.get(0);

        assertEquals(sessionDto.getId(),session.getId());
        assertEquals(sessionDto.getName(),session.getName());
        assertEquals(sessionDto.getDate(),session.getDate());
        assertEquals(sessionDto.getCreatedAt(),session.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(),session.getUpdatedAt());
        assertEquals(sessionDto.getUsers(),session.getUsers());
        assertEquals(sessionDto.getDescription(),session.getDescription());
        assertEquals(sessionDto.getTeacher_id(),session.getTeacher().getId());
    }

}
