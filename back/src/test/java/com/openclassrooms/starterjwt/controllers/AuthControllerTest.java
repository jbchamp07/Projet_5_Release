package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Optional;

import static org.mockito.Mockito.*;

public class AuthControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    /*@Test
    public void testAuthenticateUser() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        UserDetailsImpl userDetails = new UserDetailsImpl((long)1, "test@example.com", "First", "Last", true,"password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(new User("test@example.com", "Last", "First", "password", false)));
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Act
        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);

        // Assert
        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
        assert responseEntity.getStatusCode().is2xxSuccessful();
        assert jwtResponse != null;
        assert jwtResponse.getToken().equals("jwt-token");
        //assert jwtResponse.getEmail().equals("test@example.com");
        assert jwtResponse.getFirstName().equals("First");
        assert jwtResponse.getLastName().equals("Last");
        //assert !jwtResponse.isAdmin();
    }*/

    @Test
    public void testRegisterUser() {
        // Arrange
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setFirstName("First");
        signUpRequest.setLastName("Last");
        signUpRequest.setPassword("password");

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(new User(signUpRequest.getEmail(), signUpRequest.getLastName(), signUpRequest.getFirstName(), "encoded-password", false));

        // Act
        ResponseEntity<?> responseEntity = authController.registerUser(signUpRequest);

        // Assert
        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
        assert responseEntity.getStatusCode().is2xxSuccessful();
        assert messageResponse != null;
        assert messageResponse.getMessage().equals("User registered successfully!");
    }

    @Test
    public void testRegisterUser_EmailAlreadyTaken() {
        // Arrange
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setFirstName("First");
        signUpRequest.setLastName("Last");
        signUpRequest.setPassword("password");

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        // Act
        ResponseEntity<?> responseEntity = authController.registerUser(signUpRequest);

        // Assert
        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
        assert responseEntity.getStatusCode().is4xxClientError();
        assert messageResponse != null;
        assert messageResponse.getMessage().equals("Error: Email is already taken!");
    }

    //Tests MockMVC (integration)
    @Test
    void testAuthenticateUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@example.com", "John", "Doe", false, "password");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("mocked-jwt-token");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User("test@example.com", "Doe", "John", "password", false)));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    void testRegisterUserOK() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("new@example.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }
}
