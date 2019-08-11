package com.shmigel.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface BaseService<E, I> {
    Optional<E> find(I id);
    Page<E> findAll(Pageable pageable);
    E save(E entity);
    void delete(I id);
}
