package com.openclassrooms.starterjwt.controllers;


import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.openclassrooms.starterjwt.mapper.SessionMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
//TODO ajouter des tests
public class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    public SessionControllerTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById_Ok() {
        Long id = (long)1;
        Session session = new Session();
        session.setId(id);

        when(sessionService.getById(id)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(new SessionDto());

        ResponseEntity<?> responseEntity = sessionController.findById(id.toString());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(sessionService, times(1)).getById(id);
        verify(sessionMapper, times(1)).toDto(session);
    }
    @Test
    public void testFindById_NotFound() {
        when(sessionService.getById((long)1)).thenReturn(null);

        ResponseEntity<?> responseEntity = sessionController.findById("1");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }
    @Test
    public void testFindById_BadRequest() {

        doThrow(new NumberFormatException()).when(sessionService).getById((long) 1);

        ResponseEntity<?> responseEntity = sessionController.findById("1");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    public void testFindAll() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session());

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(new ArrayList<>());

        ResponseEntity<?> responseEntity = sessionController.findAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(sessionService, times(1)).findAll();
        verify(sessionMapper, times(1)).toDto(sessions);
    }

    @Test
    public void testSave_SessionNotFound() {
        when(sessionService.getById((long)1)).thenReturn(null);
        ResponseEntity<?> responseEntity = sessionController.save("1");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    @Test
    public void testSave_SessionFound() {
        when(sessionService.getById((long)1)).thenReturn(new Session());
        ResponseEntity<?> responseEntity = sessionController.save("1");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    public void testSave_SessionBadRequest() {
        when(sessionService.getById((long)1)).thenReturn(new Session());
        doThrow(new NumberFormatException()).when(sessionService).delete((long) 1);
        ResponseEntity<?> responseEntity = sessionController.save("1");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdate_SessionFound() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId((long)1);
        sessionDto.setName("Name");
        Session session = new Session();
        session.setId((long)1);
        session.setName("Name");
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update((long)1,session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> responseEntity = sessionController.update("1",sessionDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(sessionDto,responseEntity.getBody());
    }
    @Test
    public void testUpdate_SessionNotFound() {
        doThrow(new NumberFormatException()).when(sessionMapper).toEntity(new SessionDto());
        ResponseEntity<?> responseEntity = sessionController.update("1",new SessionDto());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testCreate_OK() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId((long)1);
        sessionDto.setName("Name");
        Session session = new Session();
        session.setId((long)1);
        session.setName("Name");
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> responseEntity = sessionController.create(sessionDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(sessionDto,responseEntity.getBody());
    }

    @Test
    public void testParticipate_OK() {
        doNothing().when(sessionService).participate((long)1,(long)1);
        ResponseEntity<?> responseEntity = sessionController.participate("1","1");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    public void testParticipate_BadRequest() {
        doThrow(new NumberFormatException()).when(sessionService).participate((long)1,(long)1);
        ResponseEntity<?> responseEntity = sessionController.participate("1","1");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testNoLongerParticipate_OK() {
        doNothing().when(sessionService).noLongerParticipate((long)1,(long)1);
        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate("1","1");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    public void testNoLongerParticipate_BadRequest() {
        doThrow(new NumberFormatException()).when(sessionService).noLongerParticipate((long)1,(long)1);
        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate("1","1");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
