package com.openclassrooms.starterjwt.security.jwt;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;


public class JwtUtilsTest {

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtUtils jwtUtils;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "mySecretKey");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 86400000); // 1 day
        userDetails = new UserDetailsImpl((long)1, "user", "first", "last", false,"pass");
    }

    @Test
    void generateJwtToken_shouldReturnToken() {
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void getUserNameFromJwtToken_shouldReturnUserName() {
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);
        String userName = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals("user", userName);
    }

    @Test
    void validateJwtToken_shouldReturnTrueForValidToken() {
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);

        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void validateJwtToken_shouldReturnFalseForInvalidSignature() {
        String invalidToken = "invalidToken";

        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }

    @Test
    void validateJwtToken_shouldReturnFalseForExpiredToken() {
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", -1000); // Token expired 1 second ago
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);

        assertFalse(jwtUtils.validateJwtToken(token));
    }

}
