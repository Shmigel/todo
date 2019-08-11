package com.shmigel.todo.controller;

import com.shmigel.todo.model.Task;
import com.shmigel.todo.model.User;
import com.shmigel.todo.service.AuthenticationProviderImpl;
import com.shmigel.todo.service.UserService;
import com.shmigel.todo.service.UserTaskRelationService;
import com.shmigel.todo.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private UserTaskRelationService userTaskRelation;
    private AuthenticationProviderImpl authenticationProvider;

    @Autowired
    public UserController(UserService userService,
                          UserTaskRelationService userTaskRelation,
                          AuthenticationProviderImpl authenticationProvider) {
        this.userService = userService;
        this.userTaskRelation = userTaskRelation;
        this.authenticationProvider = authenticationProvider;
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAll(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable UUID id) {
        return userService.find(id).map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with such id:"+id+" doesn't exist"));
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<Page<Task>> getAllUserTasks(@PathVariable UUID id, Pageable pageable) {
        return ResponseEntity.ok(userTaskRelation.getUserTasks(id, pageable));
    }

    @GetMapping("/me/tasks")
    public ResponseEntity<Page<Task>> getLoggedUser(Pageable pageable) {
        UUID id = authenticationProvider.credentials();
        Page<Task> userTasks = userTaskRelation.getUserTasks(id, pageable);
        return ResponseEntity.ok(userTasks);
    }

    @PostMapping("/me/tasks")
    public ResponseEntity<Task> createUserTask(@RequestBody Task task) {
        UUID id = authenticationProvider.credentials();
        Task saved = userTaskRelation.createTask(id, task);
        URI selfLink = Utils.selfLink(TaskController.class, saved.getId());
        return ResponseEntity.created(selfLink).body(saved);
    }

    @PatchMapping("/me/tasks/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable UUID taskId, @RequestBody Map<String, Object> fields) {
        UUID userId = authenticationProvider.credentials();
        Task task = userTaskRelation.updateTask(userId, taskId, fields);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/me/tasks")
    public ResponseEntity<Task> updateTask(@RequestParam String taskText, @RequestBody Map<String, Object> fields) {
        UUID userId = authenticationProvider.credentials();
        Task task = userTaskRelation.updateTask(userId, taskText, fields);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/me/tasks/{taskId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID taskId) {
        UUID userId = authenticationProvider.credentials();
        userTaskRelation.removeTask(userId, taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/me/tasks")
    public ResponseEntity<?> deleteUser(@RequestParam String taskText) {
        UUID userId = authenticationProvider.credentials();
        userTaskRelation.removeTask(userId, taskText);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
