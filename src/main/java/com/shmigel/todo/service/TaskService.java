package com.shmigel.todo.service;

import com.shmigel.todo.model.Task;
import com.shmigel.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService implements BaseService<Task, UUID> {

    @Autowired
    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Optional<Task> find(UUID id) {
        return taskRepository.findById(id);
    }

    @Override
    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public Task save(Task entity) {
        return taskRepository.save(entity);
    }

    @Override
    public void delete(UUID id) {
        taskRepository.deleteById(id);
    }

    public void update() {

    }
}
