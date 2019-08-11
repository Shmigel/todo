package com.shmigel.todo.controller;

import com.shmigel.todo.model.JwtResponse;
import com.shmigel.todo.model.User;
import com.shmigel.todo.model.UserAuthDetails;
import com.shmigel.todo.service.AuthService;
import com.shmigel.todo.service.UserService;
import com.shmigel.todo.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    private UserService userService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> currentUser() {
        UserAuthDetails userAuthDetails = (UserAuthDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userService.findByUsername(userAuthDetails.getUsername());
        return ResponseEntity.of(user);
    }

    @PostMapping("/registration")
    public ResponseEntity<JwtResponse> registration(
         @RequestBody UserAuthDetails userAuthDetails) {
        JwtResponse register = authService.register(userAuthDetails);
        URI selfLink = Utils.selfLink(UserController.class, register.getUserId());
        return ResponseEntity.created(selfLink).body(register);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody UserAuthDetails userAuthDetails) {
        JwtResponse login = authService.login(userAuthDetails);
        return ResponseEntity.ok(login);
    }

}
