package com.shmigel.todo.repository;

import com.shmigel.todo.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findAllByUserId(UUID id, Pageable pageable);
    Optional<Task> findByTextAndUserId(String text, UUID userId);
}
