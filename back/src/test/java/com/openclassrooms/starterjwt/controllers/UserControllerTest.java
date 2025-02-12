package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {
    private MockMvc mockMvc;
    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private UserDetails userDetails;
    @Mock
    private Authentication authentication;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testFindById_UserExists() {
        User user = new User();
        user.setId((long)1);
        user.setEmail("test@example.com");
        UserDto userDto = new UserDto();
        userDto.setId((long)1);
        userDto.setEmail("test@example.com");

        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    public void testFindById_UserDoesNotExist() {
        when(userService.findById((long)1)).thenReturn(null);

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindById_InvalidId() {
        ResponseEntity<?> response = userController.findById("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    @WithMockUser(username = "test@example.com")
    public void testDelete_UserExistsAndAuthorized() {
        User user = new User();
        user.setId((long)1);
        user.setEmail("test@example.com");

        when(userService.findById((long)1)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        ResponseEntity<?> response = userController.save("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).delete(1L);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testDelete_UserExistsButUnauthorized() {
        User user = new User();
        user.setId((long)1);
        user.setEmail("another@example.com");

        when(userService.findById((long)1)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        ResponseEntity<?> response = userController.save("1");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService, never()).delete(anyLong());
    }

    @Test
    public void testDelete_UserDoesNotExist() {
        when(userService.findById((long)1)).thenReturn(null);

        ResponseEntity<?> response = userController.save("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDelete_InvalidId() {
        ResponseEntity<?> response = userController.save("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    //Tests mockmvc
    @Test
    void testFindById_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        UserDto userDto = new UserDto();

        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(userService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindById_BadRequest() throws Exception {
        mockMvc.perform(get("/api/user/abc"))
                .andExpect(status().isBadRequest());
    }

    /*@Test
    void testDelete_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());
    }*/

    @Test
    void testDelete_NotFound() throws Exception {
        when(userService.findById(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    /*@Test
    void testDelete_Unauthorized() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("other@example.com");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isUnauthorized());
    }*/
}
