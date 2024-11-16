package com.example.taskmanagementsystem.repository;

import com.example.taskmanagementsystem.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByAuthorId(Integer authorId);
    Page<Task> findByAuthorId(Long authorId, Pageable pageable);
    List<Task> findByAssigneeId(Long assigneeId);
    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);
}
