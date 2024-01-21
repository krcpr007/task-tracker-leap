package com.krcpr007.taskTracker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.krcpr007.taskTracker.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findById(Long userId);

    Page<Task> findByCreatedByOrderByDateDesc(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);

    List<Task> findByIdIn(List<Long> taskIds);

    List<Task> findByIdIn(List<Long> pollIds, Sort sort);
}
