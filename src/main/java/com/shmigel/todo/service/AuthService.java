package com.shmigel.todo.service;

import com.shmigel.todo.model.JwtResponse;
import com.shmigel.todo.model.User;
import com.shmigel.todo.model.UserAuthDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    public AuthService(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public JwtResponse register(UserAuthDetails userAuthDetails) {
        if (userService.existsByUsername(userAuthDetails.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User with such username:"+userAuthDetails.getUsername()+" already exist");
        User save = userService.save(new User(userAuthDetails));
        String authToken = jwtService.generateToken(new UserAuthDetails(save));
        long tokenExpireIn = tokenExpiration(
                jwtService.getExpirationDateFromToken(authToken));
        return new JwtResponse(authToken, tokenExpireIn, save.getId());
    }

    public JwtResponse login(UserAuthDetails userAuthDetails) {
        User user = userService.findByUsername(userAuthDetails.getUsername())
                .filter(foundUser -> userAuthDetails.getPassword().equals(foundUser.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Either username or password is incorrect"));

        String authToken = jwtService.generateToken(userAuthDetails);
        long tokenExpiration = tokenExpiration(jwtService.getExpirationDateFromToken(authToken));
        return new JwtResponse(authToken, tokenExpiration, user.getId());
    }

    public long tokenExpiration(Date tokenExpireDate) {
        return tokenExpireDate.getTime() - System.currentTimeMillis();
    }
}
