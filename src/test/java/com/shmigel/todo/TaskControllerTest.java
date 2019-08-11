package com.shmigel.todo;

import com.shmigel.todo.controller.TaskController;
import com.shmigel.todo.model.Task;
import com.shmigel.todo.service.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TaskControllerTest {

    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @Before
    public void setUp() {
        taskController = new TaskController(taskService);
    }

    @Test(expected = ResponseStatusException.class)
    public void taskNotFoundTest() {
        when(taskService.find(any())).thenReturn(Optional.empty());
        ResponseEntity<Task> task = taskController.getTask(UUID.randomUUID());
        assertEquals(task.getStatusCodeValue(), 404);
    }

}
