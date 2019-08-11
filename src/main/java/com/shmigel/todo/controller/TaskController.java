package com.shmigel.todo.controller;

import com.shmigel.todo.model.Task;
import com.shmigel.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable UUID id) {
        return taskService.find(id).map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Task with such id:"+id+" wasn't found"));
    }

    @GetMapping
    public ResponseEntity<Page<Task>> getAllTasks(Pageable pageable) {
        return ResponseEntity.ok(taskService.findAll(pageable));
    }

}
