package com.shmigel.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shmigel.todo.model.JwtResponse;
import com.shmigel.todo.model.User;
import com.shmigel.todo.model.UserAuthDetails;
import com.shmigel.todo.service.JwtService;
import com.shmigel.todo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.StringUtils;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Test
    public void registrationTest() throws Exception {
        String userJson = "{\n" +
                "\t\"username\": \"IOASe\",\n" +
                "\t\"password\": \"pass\"\n" +
                "}";
        String asyncResult = mockMvc.perform(post("/auth/registration")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userJson))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();
        JwtResponse jwtResponse = objectMapper.readValue(asyncResult, JwtResponse.class);

        StringUtils.hasText(asyncResult);
        assertTrue(jwtService.validateToken(jwtResponse.getAuthToken()));
    }

    @Test
    public void meEndpointTest() throws Exception {
        User user = new User(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        UserAuthDetails userAuthDetails = new UserAuthDetails(userService.save(user));
        String token = jwtService.generateToken(userAuthDetails);

        mockMvc.perform(get("/auth/me").header("Authorization", "Bearer "+token))
                .andExpect(matchAll(
                        status().isOk(),
                        jsonPath("$.username").value(user.getUsername())
                ));
    }

    @Test
    public void wrongPasswordTest() throws Exception {
        User user = new User("Sh123", "pass");
        userService.save(user);
        String userJson = "{\n" +
                "\t\"username\": \""+user.getUsername()+"\",\n" +
                "\t\"password\": \""+user.getPassword()+"1\"\n" +
                "}";

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userJson))
                .andExpect(status().is4xxClientError());
    }

}
