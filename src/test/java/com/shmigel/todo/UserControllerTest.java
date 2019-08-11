package com.shmigel.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shmigel.todo.controller.UserController;
import com.shmigel.todo.model.JwtResponse;
import com.shmigel.todo.model.User;
import com.shmigel.todo.model.UserAuthDetails;
import com.shmigel.todo.repository.UserRepository;
import com.shmigel.todo.service.AuthenticationProviderImpl;
import com.shmigel.todo.service.JwtService;
import com.shmigel.todo.service.UserService;
import com.shmigel.todo.service.UserTaskRelationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserTaskRelationService userTaskRelationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationProviderImpl authenticationProvider;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        userController = new UserController(userService, userTaskRelationService, authenticationProvider);
    }

    @Test(expected = ResponseStatusException.class)
    public void userNotFoundTest() {
        when(userService.find(any())).thenReturn(Optional.empty());
        ResponseEntity<User> user = userController.getUser(UUID.randomUUID());
        assertTrue(user.getStatusCode().is4xxClientError());
    }

    @Test
    public void passwordIsNotPresent() throws Exception {
        String userJson = "{\n" +
                "\t\"username\": \"USe1\",\n" +
                "\t\"password\": \"pas1\"\n" +
                "}";
        String asyncResult = mockMvc.perform(post("/auth/registration")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userJson))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();
        JwtResponse jwtResponse = objectMapper.readValue(asyncResult, JwtResponse.class);

        mockMvc.perform(get("/auth/me")
                .header("Authorization", "Bearer "+jwtResponse.getAuthToken())
        ).andExpect(matchAll(
                status().isOk(),
                jsonPath("$.id").exists(),
                jsonPath("$.password").doesNotExist()
        ));
    }

    @Test
    public void dentAllowTaskDuplicates() throws Exception {
        User user = userRepository.save(
                new User(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
        System.out.println(user);
        String token = jwtService.generateToken(new UserAuthDetails(user));
        String json = "{\n" +
                "\t\"text\": \"task\",\n" +
                "\t\"done\": false\n" +
                "}";

        mockMvc.perform(post("/users/me/tasks")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().is(201));

        mockMvc.perform(post("/users/me/tasks")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(matchAll(
                        status().is4xxClientError(),
                        jsonPath("$.message")
                                .value("An error occurs while executing sql query")
                ));
    }

}
