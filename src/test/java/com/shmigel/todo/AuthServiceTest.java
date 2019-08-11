package com.shmigel.todo;

import com.shmigel.todo.model.JwtResponse;
import com.shmigel.todo.model.User;
import com.shmigel.todo.model.UserAuthDetails;
import com.shmigel.todo.service.AuthService;
import com.shmigel.todo.service.JwtService;
import com.shmigel.todo.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AuthServiceTest {

    @Mock
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    private AuthService authService;

    @Before
    public void setUp() {
        authService = new AuthService(jwtService, userService);
    }

    @Test
    public void registerNewUserTest() {
        when(userService.existsByUsername(anyString())).thenReturn(false);
        when(userService.save(any())).thenAnswer(u -> {
            User user = (User) u.getArgument(0);
            user.setId(UUID.randomUUID());
            return user;
        });
        UserAuthDetails userAuthDetails = new UserAuthDetails("Use", "Pas");
        JwtResponse jwtResponse = authService.register(userAuthDetails);
        String usernameFromToken = jwtService.getUsernameFromToken(jwtResponse.getAuthToken());
        assertNotNull(usernameFromToken);
        assertEquals(userAuthDetails.getUsername(), usernameFromToken);
    }

    @Test(expected = ResponseStatusException.class)
    public void registerExistingUser() {
        when(userService.existsByUsername(anyString())).thenReturn(true);
        UserAuthDetails userAuthDetails = new UserAuthDetails("name", "pass");
        authService.register(userAuthDetails);
    }

    @Test
    public void validateCreatedTokenTest() {
        String s = jwtService.generateToken(new UserAuthDetails("Sh", "hs"));
        assertTrue(jwtService.validateToken(s));
    }

}
