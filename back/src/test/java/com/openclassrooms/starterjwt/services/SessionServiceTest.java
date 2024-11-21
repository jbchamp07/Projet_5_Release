package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SessionServiceTest {

    @InjectMocks
    private SessionService sessionService;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;

    public SessionServiceTest() {
        MockitoAnnotations.initMocks(this);
    }
    private Session session;
    private User user;
    @BeforeEach
    public void setUp() {
        session = new Session().setId((long)1).setUsers(new ArrayList<>());
        user = new User().setId((long)2);
    }
    @Test
    public void testPartcipate_OK(){

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        sessionService.participate(session.getId(),user.getId());

        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).findById(session.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testPartcipate_SessionNotFound(){

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(session.getId(), user.getId());
        });

        verify(sessionRepository, times(1)).findById(session.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testPartcipate_UserNotFound(){

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(session.getId(), user.getId());
        });

        verify(sessionRepository, times(1)).findById(session.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testParticipate_AlreadyParticipate() {
        session.getUsers().add(user);
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(session.getId(), user.getId());
        });

        verify(sessionRepository, times(1)).findById(session.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(sessionRepository, never()).save(any(Session.class));
    }


    @Test
    public void TestNoLongerParticipate_OK(){
        session.getUsers().add(user);
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(session.getId(),user.getId());

        verify(sessionRepository,times(1)).findById(session.getId());
        verify(sessionRepository, times(1)).save(any(Session.class));
    }
    @Test
    public void TestNoLongerParticipate_SessionNotFound(){
        session.getUsers().add(user);
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            sessionService.noLongerParticipate(session.getId(), user.getId());
        });

        verify(sessionRepository, times(1)).findById(session.getId());
        verify(sessionRepository, never()).save(any(Session.class));
    }
    @Test
    public void TestNoLongerParticipate_BadRequest(){

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> {
            sessionService.noLongerParticipate(session.getId(), user.getId());
        });

        verify(sessionRepository, times(1)).findById(session.getId());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void TestUpdate_Ok(){

        when(sessionRepository.save(session)).thenReturn(session);

        Session s = sessionService.update(session.getId(),session);
        assertEquals(session,s);

    }
    @Test
    public void TestGetById_Ok(){

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.ofNullable(session));

        Session s = sessionService.getById(session.getId());
        assertEquals(session,s);

    }
    @Test
    public void TestFindAll_Ok(){
        List<Session> list = new ArrayList<Session>();
        list.add(session);
        when(sessionRepository.findAll()).thenReturn(list);

        List<Session> listToTest = sessionService.findAll();

        assertEquals(session,listToTest.get(0));

    }
}
