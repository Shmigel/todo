package com.shmigel.todo.service;

import com.shmigel.todo.model.Task;
import com.shmigel.todo.model.User;
import com.shmigel.todo.repository.TaskRepository;
import com.shmigel.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class UserTaskRelationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserTaskRelationService self;

    public UserTaskRelationService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public Task createTask(UUID id, Task task) {
        return userRepository.findById(id).map(u -> {
            u.addTask(task);
            return taskRepository.save(task);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User with such id:"+id+" doesn't exist"));
    }

    public void removeTask(UUID userId, String taskText) {
        taskRepository.findByTextAndUserId(taskText, userId)
                .ifPresent(t -> self.removeTask(userId, t.getId()));
    }

    public void removeTask(UUID userId, UUID taskId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with such id:" + userId + " doesn't exist"));
        user.getTasks().removeIf(t -> t.getId().equals(taskId));
    }

    public Task updateTask(UUID userId, String taskText, Map<String, Object> fields) {
        return taskRepository.findByTextAndUserId(taskText, userId)
                .map(t -> self.updateTask(userId, t.getId(), fields))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User:"+userId+"doesn't have task with text:"+taskText));
    }

    public Task updateTask(UUID userId, UUID taskId, Map<String, Object> fields) {
        List<Task> tasks = userRepository.findById(userId)
                .map(User::getTasks).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with such id:" + userId + " doesn't exist"));
        return tasks.stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst().map(t -> updateTaskFields(t, fields))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Task with such id:" + taskId + " doesn't exist"));
    }

    private Task updateTaskFields(Task task, Map<String, Object> fields) {
        List<String> notExistingFields = new ArrayList<>();
        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Task.class, k);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, task, v);
            } else {
                notExistingFields.add(k);
            }
        });

        if (!notExistingFields.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Following fields wasn't found"+
                            String.join(",", notExistingFields));
        } else
            return taskRepository.save(task);
    }

    public Page<Task> getUserTasks(UUID userId, Pageable pageable) {
        return taskRepository.findAllByUserId(userId, pageable);
    }

}
