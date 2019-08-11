package com.shmigel.todo;

import com.shmigel.todo.model.UserAuthDetails;
import com.shmigel.todo.service.JwtService;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Value("{jwt.secret}")
    private String secret;

    @Test
    public void createdJwtContainsProperSubject() {
        UserAuthDetails userAuthDetails = new UserAuthDetails("UserName", "qwerty");
        String generatedToken = jwtService.generateToken(userAuthDetails);
        String subject = Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(generatedToken).getBody().getSubject();
        assertEquals(userAuthDetails.getUsername(), subject);
    }

}
