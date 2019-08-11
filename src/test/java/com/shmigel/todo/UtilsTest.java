package com.shmigel.todo;

import com.shmigel.todo.controller.UserController;
import com.shmigel.todo.utils.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.UUID;

@RunWith(SpringRunner.class)
public class UtilsTest {

    @Test
    public void selfLinkCreatingTest() {
        UUID uuid = UUID.randomUUID();
        URI uri = Utils.selfLink(UserController.class, uuid);
        System.out.println(uri.toString());
    }

}
